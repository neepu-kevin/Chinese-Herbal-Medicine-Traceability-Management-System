<template>
  <div class="app-container tcm-page">
    <div class="tcm-hero mini">
      <h2><i class="el-icon-time" /> 种植加工管理</h2>
      <p>按时间顺序记录种植过程，包括种子、种植、施肥、用药、采收、初加工、包装等环节，支持添加、查询，可按批次ID筛选。</p>
    </div>

    <el-alert title="过程记录仅支持新增提交，提交后不可修改或删除" type="warning" show-icon :closable="false" style="margin-bottom: 10px;" />

    <el-card shadow="never" class="tcm-trace-card">
      <el-form :inline="true" size="small" class="mb8">
        <el-form-item label="批次ID">
          <el-input-number v-model="query.batchId" :min="1" :controls="false" placeholder="全部" clearable @change="getList" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="small" @click="getList">查询</el-button>
          <el-button type="primary" plain icon="el-icon-plus" size="small" @click="openAdd">添加记录</el-button>
        </el-form-item>
      </el-form>

      <el-table v-loading="loading" :data="list" stripe>
        <el-table-column prop="process_id" label="记录ID" width="90" align="center" />
        <el-table-column prop="batch_id" label="批次ID" width="90" align="center" />
        <el-table-column label="过程类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="small" type="success">{{ typeLabel(scope.row.process_type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="process_content" label="内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="process_time" label="处理时间" width="170" />
        <el-table-column prop="operator_name" label="操作人" width="120" />
      </el-table>
    </el-card>

    <el-dialog title="添加过程记录" :visible.sync="open" width="580px" append-to-body destroy-on-close>
      <el-form ref="form" :model="form" label-width="110px">
        <el-form-item label="批次ID" required>
          <el-input-number v-model="form.batchId" :min="1" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="过程类型" required>
          <el-select v-model="form.processType" placeholder="选择标准阶段" filterable style="width: 100%;">
            <el-option v-for="k in stageKeys" :key="k" :label="typeLabel(k)" :value="k" />
          </el-select>
        </el-form-item>
        <el-form-item label="记录内容" required>
          <el-input v-model="form.processContent" type="textarea" :rows="4" placeholder="详细描述该环节的具体操作和参数" />
        </el-form-item>
        <el-form-item label="处理时间" required>
          <el-date-picker v-model="form.processTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="操作人">
          <el-input v-model="form.operatorName" maxlength="64" placeholder="填写操作人姓名" />
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
import { listProcess, addProcess } from '@/api/tcm/traceability'
import { PROCESS_STAGE_ORDER, getProcessTypeLabel } from '@/utils/tcm'

export default {
  name: 'TcmProcess',
  data() {
    return {
      loading: false,
      open: false,
      list: [],
      query: { batchId: undefined },
      form: {}
    }
  },
  computed: {
    stageKeys() {
      return PROCESS_STAGE_ORDER
    }
  },
  created() {
    this.getList()
  },
  methods: {
    typeLabel(t) {
      return getProcessTypeLabel(t)
    },
    getList() {
      this.loading = true
      const q = {}
      if (this.query.batchId) q.batchId = this.query.batchId
      listProcess(q).then(res => {
        this.list = res.rows || []
        this.loading = false
      })
    },
    openAdd() {
      this.form = { batchId: this.query.batchId || undefined }
      this.open = true
    },
    submit() {
      addProcess(this.form).then(() => {
        this.$modal.msgSuccess('提交成功')
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
