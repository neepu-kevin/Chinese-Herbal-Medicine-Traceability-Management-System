<template>
  <div class="app-container tcm-page">
    <div class="tcm-hero mini">
      <h2><i class="el-icon-chat-dot-round" /> 评价管理</h2>
      <p>对已发布的批次进行产品质量和企业服务双维度评价，为其他消费者提供参考，同时帮助企业改进质量。</p>
    </div>

    <el-card shadow="never" class="tcm-trace-card">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-edit-outline" size="small" @click="openAdd">提交评价</el-button>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="list" stripe>
        <el-table-column prop="review_id" label="评价ID" width="90" align="center" />
        <el-table-column prop="batch_id" label="批次ID" width="90" align="center" />
        <el-table-column label="产品评分" width="100" align="center">
          <template slot-scope="scope">
            <el-rate :value="scope.row.product_score" disabled show-score text-color="#ff9900" />
          </template>
        </el-table-column>
        <el-table-column label="企业评分" width="100" align="center">
          <template slot-scope="scope">
            <el-rate :value="scope.row.enterprise_score" disabled show-score text-color="#ff9900" />
          </template>
        </el-table-column>
        <el-table-column prop="review_content" label="评价内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="create_time" label="评价时间" width="170" />
      </el-table>
    </el-card>

    <el-dialog title="提交评价" :visible.sync="open" width="520px" append-to-body destroy-on-close>
      <el-form ref="form" :model="form" label-width="100px">
        <el-form-item label="批次ID" required>
          <el-input-number v-model="form.batchId" :min="1" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="产品评分">
          <el-rate v-model="form.productScore" :max="5" show-text :texts="['很差','较差','一般','满意','非常满意']" />
        </el-form-item>
        <el-form-item label="企业评分">
          <el-rate v-model="form.enterpriseScore" :max="5" show-text :texts="['很差','较差','一般','满意','非常满意']" />
        </el-form-item>
        <el-form-item label="评价内容">
          <el-input v-model="form.reviewContent" type="textarea" :rows="4" maxlength="500" show-word-limit />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="open = false">取消</el-button>
        <el-button type="primary" @click="submit">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listReview, addReview } from '@/api/tcm/traceability'

export default {
  name: 'TcmReview',
  data() {
    return { loading: false, open: false, list: [], form: {} }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listReview({}).then(res => {
        this.list = res.rows || []
        this.loading = false
      })
    },
    openAdd() {
      this.form = { productScore: 5, enterpriseScore: 5, reviewContent: '' }
      this.open = true
    },
    submit() {
      addReview(this.form).then(() => {
        this.$modal.msgSuccess('评价成功')
        this.open = false
        this.getList()
      })
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
