<template>
  <div class="app-container tcm-page">
    <div class="tcm-hero mini">
      <h2><i class="el-icon-goods" /> 中草药产品管理</h2>
      <p>维护企业所生产的中草药产品名录（品类、规格、功效说明）、库存状态以及生成溯源码（批次码）。</p>
    </div>

    <el-card shadow="never" class="tcm-trace-card">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-plus" size="small" @click="openAdd">新增产品</el-button>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="list" stripe>
        <el-table-column prop="product_id" label="产品ID" width="90" align="center" />
        <el-table-column prop="product_code" label="产品编码" width="160" show-overflow-tooltip />
        <el-table-column prop="product_name" label="产品名称" min-width="140" show-overflow-tooltip />
        <el-table-column prop="origin_place" label="产地" width="120" show-overflow-tooltip />
        <el-table-column prop="stock_quantity" label="库存" width="90" align="center" />
        <el-table-column label="操作" width="140" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="mini" icon="el-icon-edit" @click="openEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="mini" icon="el-icon-delete" @click="remove(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="title" :visible.sync="open" width="520px" append-to-body destroy-on-close>
      <el-form ref="form" :model="form" label-width="100px">
        <el-form-item label="产品编码" required>
          <el-input v-model="form.productCode" maxlength="64" placeholder="全局唯一编码" />
        </el-form-item>
        <el-form-item label="产品名称" required>
          <el-input v-model="form.productName" maxlength="100" />
        </el-form-item>
        <el-form-item label="产地">
          <el-input v-model="form.originPlace" maxlength="100" placeholder="例如:安徽黄山" />
        </el-form-item>
        <el-form-item label="库存数量">
          <el-input-number v-model="form.stockQuantity" :min="0" style="width: 100%;" />
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
import { listProduct, addProduct, updateProduct, delProduct } from '@/api/tcm/traceability'

export default {
  name: 'TcmProduct',
  data() {
    return { loading: false, list: [], open: false, title: '', form: {} }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listProduct().then(res => {
        this.list = res.rows || []
        this.loading = false
      })
    },
    openAdd() {
      this.title = '新增产品'
      this.form = { stockQuantity: 0 }
      this.open = true
    },
    openEdit(row) {
      this.title = '编辑产品'
      this.form = {
        productId: row.product_id,
        productCode: row.product_code,
        productName: row.product_name,
        originPlace: row.origin_place,
        stockQuantity: row.stock_quantity
      }
      this.open = true
    },
    submit() {
      const api = this.form.productId ? updateProduct : addProduct
      api(this.form).then(() => {
        this.$modal.msgSuccess('保存成功')
        this.open = false
        this.getList()
      })
    },
    remove(row) {
      this.$modal.confirm('确认删除该产品？').then(() => delProduct(row.product_id)).then(() => {
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
