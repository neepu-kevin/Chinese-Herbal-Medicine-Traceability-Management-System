<template>
  <div class="app-container tcm-page">
    <div class="tcm-hero mini">
      <h2><i class="el-icon-office-building" /> 种植基地管理</h2>
      <p>管理种植基地的基本信息，包括地理位置、面积、土壤/水源检测报告等，为溯源提供源头环境背书。</p>
    </div>

    <el-card shadow="never" class="tcm-trace-card">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-plus" size="small" @click="openAdd">新增基地</el-button>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="list" stripe>
        <el-table-column prop="base_id" label="基地ID" width="90" align="center" />
        <el-table-column prop="base_name" label="基地名称" min-width="140" show-overflow-tooltip />
        <el-table-column prop="detail_address" label="详细地址" min-width="200" show-overflow-tooltip />
        <el-table-column prop="area_size" label="面积(亩)" width="100" align="center" />
        <el-table-column prop="remark" label="备注" min-width="120" show-overflow-tooltip />
        <el-table-column label="操作" width="140" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="mini" icon="el-icon-edit" @click="openEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="mini" icon="el-icon-delete" @click="remove(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="title" :visible.sync="open" width="560px" append-to-body destroy-on-close>
      <el-form ref="form" :model="form" label-width="100px">
        <el-form-item label="基地名称" required>
          <el-input v-model="form.baseName" maxlength="100" show-word-limit placeholder="例如:XX 生态种植基地" />
        </el-form-item>
        <el-form-item label="详细地址">
          <el-input v-model="form.detailAddress" type="textarea" :rows="2" placeholder="省市区 + 详细地址" />
        </el-form-item>
        <el-form-item label="面积(亩)">
          <el-input-number v-model="form.areaSize" :min="0" :precision="2" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="土壤/水源检测报告等信息" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="open = false">取消</el-button>
        <el-button type="primary" @click="submit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listBase, addBase, updateBase, delBase } from '@/api/tcm/traceability'

export default {
  name: 'TcmBase',
  data() {
    return { loading: false, list: [], open: false, title: '', form: {} }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listBase().then(res => {
        this.list = res.rows || []
        this.loading = false
      })
    },
    openAdd() {
      this.title = '新增基地'
      this.form = { areaSize: 0 }
      this.open = true
    },
    openEdit(row) {
      this.title = '编辑基地'
      this.form = {
        baseId: row.base_id,
        baseName: row.base_name,
        detailAddress: row.detail_address,
        areaSize: row.area_size,
        remark: row.remark
      }
      this.open = true
    },
    submit() {
      const api = this.form.baseId ? updateBase : addBase
      api(this.form).then(() => {
        this.$modal.msgSuccess('保存成功')
        this.open = false
        this.getList()
      })
    },
    remove(row) {
      this.$modal.confirm('确认删除该基地？').then(() => delBase(row.base_id)).then(() => {
        this.$modal.msgSuccess('删除成功')
        this.getList()
      }).catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/assets/styles/tcm-theme.scss';

.tcm-hero.mini {
  padding: 20px 24px;
  margin-bottom: 16px;

  h2 {
    font-size: 18px;
  }

  p {
    font-size: 13px;
  }
}
</style>
