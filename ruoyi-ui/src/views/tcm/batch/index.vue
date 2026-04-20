<template>
  <div class="app-container tcm-page">
    <div class="tcm-hero mini">
      <h2><i class="el-icon-files" /> 批次管理</h2>
      <p>选择种植基地、产品、设置批次，记录播种/收获时间等信息，生成唯一溯源码和二维码。未发布状态可编辑、发布或删除，已发布后只能查看和复制溯源码。</p>
    </div>

    <el-card shadow="never" class="tcm-trace-card">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-plus" size="small" @click="openAdd">新增批次</el-button>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="list" stripe>
        <el-table-column prop="batch_id" label="批次ID" width="90" align="center" />
        <el-table-column prop="batch_no" label="批次号" width="180" show-overflow-tooltip />
        <el-table-column prop="product_name" label="产品" min-width="120" show-overflow-tooltip />
        <el-table-column prop="base_name" label="基地" min-width="120" show-overflow-tooltip />
        <el-table-column label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.publish_status === '1' ? 'success' : 'info'" size="small">
              {{ scope.row.publish_status === '1' ? '已发布' : '未发布' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="trace_code" label="溯源码" min-width="220" show-overflow-tooltip>
          <template slot-scope="scope">
            <span v-if="scope.row.trace_code" class="mono">{{ scope.row.trace_code }}</span>
            <span v-else class="muted">-</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" align="center" fixed="right">
          <template slot-scope="scope">
            <template v-if="scope.row.publish_status !== '1'">
              <el-button type="text" size="mini" @click="openEdit(scope.row)">编辑</el-button>
              <el-button type="text" size="mini" @click="publish(scope.row)">发布溯源</el-button>
              <el-button type="text" size="mini" @click="remove(scope.row)">删除</el-button>
            </template>
            <template v-else>
              <el-button type="text" size="mini" icon="el-icon-document-copy" @click="copyText(scope.row.trace_code)">复制溯源码</el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body destroy-on-close @open="loadOptions">
      <el-form ref="form" :model="form" label-width="110px">
        <el-form-item label="种植基地" required>
          <el-select v-model="form.baseId" placeholder="选择种植基地" filterable style="width: 100%;">
            <el-option v-for="b in baseOptions" :key="b.base_id" :label="b.base_name + ' (ID:' + b.base_id + ')'" :value="b.base_id" />
          </el-select>
        </el-form-item>
        <el-form-item label="产品" required>
          <el-select v-model="form.productId" placeholder="选择产品" filterable style="width: 100%;">
            <el-option v-for="p in productOptions" :key="p.product_id" :label="p.product_name + ' / ' + p.product_code" :value="p.product_id" />
          </el-select>
        </el-form-item>
        <el-form-item label="批次号" required>
          <el-input v-model="form.batchNo" maxlength="64" placeholder="企业内部编号，要求全局唯一" />
        </el-form-item>
        <el-form-item label="播种日期">
          <el-date-picker v-model="form.sowingDate" value-format="yyyy-MM-dd" type="date" placeholder="选择" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="收获日期">
          <el-date-picker v-model="form.harvestDate" value-format="yyyy-MM-dd" type="date" placeholder="选择" style="width: 100%;" />
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
import { listBatch, addBatch, updateBatch, delBatch, publishBatch, listBase, listProduct } from '@/api/tcm/traceability'

export default {
  name: 'TcmBatch',
  data() {
    return {
      loading: false,
      list: [],
      open: false,
      title: '',
      form: {},
      baseOptions: [],
      productOptions: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listBatch().then(res => {
        this.list = res.rows || []
        this.loading = false
      })
    },
    loadOptions() {
      listBase().then(res => { this.baseOptions = res.rows || [] })
      listProduct().then(res => { this.productOptions = res.rows || [] })
    },
    openAdd() {
      this.title = '新增批次'
      this.form = {}
      this.open = true
    },
    openEdit(row) {
      this.title = '编辑批次'
      this.form = {
        batchId: row.batch_id,
        baseId: row.base_id,
        productId: row.product_id,
        batchNo: row.batch_no,
        sowingDate: row.sowing_date,
        harvestDate: row.harvest_date,
        processStage: row.process_stage
      }
      this.open = true
    },
    submit() {
      const api = this.form.batchId ? updateBatch : addBatch
      api(this.form).then(() => {
        this.$modal.msgSuccess('保存成功')
        this.open = false
        this.getList()
      })
    },
    publish(row) {
      this.$modal.confirm('确认发布该批次？发布后将生成溯源码，且不再允许删除或修改。').then(() => {
        return publishBatch(row.batch_id)
      }).then(res => {
        const code = res.data && res.data.traceCode
        this.$modal.msgSuccess(code ? '发布成功，溯源码：' + code : '发布成功')
        this.getList()
      }).catch(() => {})
    },
    remove(row) {
      this.$modal.confirm('确认删除该批次？').then(() => delBatch(row.batch_id)).then(() => {
        this.$modal.msgSuccess('删除成功')
        this.getList()
      }).catch(() => {})
    },
    copyText(text) {
      if (!text) return
      if (navigator.clipboard) {
        navigator.clipboard.writeText(text).then(() => this.$message.success('已复制'))
      }
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

.mono {
  font-family: Consolas, monospace;
  font-size: 12px;
}

.muted {
  color: #c0c4cc;
}
</style>
