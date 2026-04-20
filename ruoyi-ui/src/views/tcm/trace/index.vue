<template>
  <div class="app-container tcm-page">
    <div class="tcm-hero trace-hero">
      <h2><i class="el-icon-view" /> 中草药溯源查询</h2>
      <p>输入 32 位溯源码，可查看从种植到加工到包装的全流程信息，支持扫描二维码查询。</p>
      <el-form :inline="true" class="trace-search" @submit.native.prevent="search">
        <el-form-item label="">
          <el-input
            v-model="traceCode"
            placeholder="请输入 32 位溯源码"
            clearable
            maxlength="32"
            show-word-limit
            style="width: min(420px, 92vw);"
            @keyup.enter.native="search"
          >
            <el-button slot="append" icon="el-icon-search" @click="search">查询</el-button>
          </el-input>
        </el-form-item>
      </el-form>
    </div>

    <template v-if="detail">
      <el-card shadow="never" class="tcm-trace-card" style="margin-bottom: 16px;">
        <div slot="header">
          <span>溯源信息</span>
          <el-tag type="success" size="small" style="margin-left: 10px;">已发布</el-tag>
        </div>
        <el-row :gutter="20">
          <el-col :xs="24" :md="8" class="tcm-qrcode-box">
            <div class="tcm-section-title" style="border: none; padding: 0; margin-bottom: 12px;">溯源码二维码</div>
            <img v-if="detail.qr_code_base64" :src="detail.qr_code_base64" alt="溯源码二维码" />
            <p v-else class="muted">无二维码图片</p>
            <p class="code-text">{{ detail.trace_code }}</p>
            <el-button size="mini" icon="el-icon-document-copy" @click="copyCode(detail.trace_code)">复制溯源码</el-button>
          </el-col>
          <el-col :xs="24" :md="16">
            <div class="tcm-section-title">产品基本信息</div>
            <el-descriptions :column="2" border size="small">
              <el-descriptions-item label="溯源码">{{ detail.trace_code }}</el-descriptions-item>
              <el-descriptions-item label="批次号">{{ detail.batch_no }}</el-descriptions-item>
              <el-descriptions-item label="产品名称">{{ detail.product_name }}</el-descriptions-item>
              <el-descriptions-item label="产品编码">{{ detail.product_code }}</el-descriptions-item>
              <el-descriptions-item label="产地">{{ detail.origin_place || '-' }}</el-descriptions-item>
              <el-descriptions-item label="种植基地">{{ detail.base_name }}</el-descriptions-item>
              <el-descriptions-item label="基地地址" :span="2">{{ formatAddress(detail) }}</el-descriptions-item>
            </el-descriptions>
          </el-col>
        </el-row>
      </el-card>

      <el-card shadow="never" class="tcm-trace-card" style="margin-bottom: 16px;">
        <div slot="header">种植加工流程（全链路）</div>
        <el-steps :active="activeStepIndex" finish-status="success" align-center>
          <el-step
            v-for="(key, idx) in stageKeys"
            :key="key"
            :title="stageTitle(key)"
            :description="statusForStage(key)"
          />
        </el-steps>
        <p class="hint">流程包含环节、时间、操作人等记录；若某环节未记录，则显示为未处理（灰色节点）。</p>
      </el-card>

      <el-card shadow="never" class="tcm-trace-card" style="margin-bottom: 16px;">
        <div slot="header">详细时间线</div>
        <el-timeline v-if="detail.processList && detail.processList.length">
          <el-timeline-item
            v-for="(item, index) in detail.processList"
            :key="index"
            :timestamp="item.process_time"
            placement="top"
          >
            <el-card shadow="hover">
              <span class="tcm-timeline-type">{{ typeLabel(item.process_type) }}</span>
              <p class="mb0">{{ item.process_content }}</p>
              <p class="muted small">操作人：{{ item.operator_name || '-' }}</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
        <el-empty v-else description="无流程记录" />
      </el-card>

      <el-card shadow="never" class="tcm-trace-card">
        <div slot="header">消费者评价数据</div>
        <el-row :gutter="16">
          <el-col :span="8">
            <div class="score-box">
              <div class="label">产品评分</div>
              <div class="num">{{ scoreFmt(detail.score && detail.score.product_score_avg) }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="score-box">
              <div class="label">企业评分</div>
              <div class="num">{{ scoreFmt(detail.score && detail.score.enterprise_score_avg) }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="score-box">
              <div class="label">评价数量</div>
              <div class="num">{{ (detail.score && detail.score.review_count) || 0 }}</div>
            </div>
          </el-col>
        </el-row>
      </el-card>
    </template>

    <el-empty v-else-if="searched" description="未查询到该溯源码，请检查输入" />
  </div>
</template>

<script>
import { queryTrace } from '@/api/tcm/traceability'
import { PROCESS_STAGE_ORDER, getProcessTypeLabel } from '@/utils/tcm'

export default {
  name: 'TcmTrace',
  data() {
    return {
      traceCode: '',
      detail: null,
      searched: false
    }
  },
  computed: {
    stageKeys() {
      return PROCESS_STAGE_ORDER
    },
    activeStepIndex() {
      if (!this.detail || !this.detail.processList) return 0
      const types = new Set(this.detail.processList.map(p => p.process_type))
      let max = -1
      PROCESS_STAGE_ORDER.forEach((k, i) => {
        if (types.has(k)) max = i
      })
      return max + 1
    }
  },
  methods: {
    typeLabel(t) {
      return getProcessTypeLabel(t)
    },
    stageTitle(key) {
      return getProcessTypeLabel(key)
    },
    statusForStage(key) {
      if (!this.detail || !this.detail.processList) return ''
      const hit = this.detail.processList.some(p => p.process_type === key)
      return hit ? '已登记' : '未登记'
    },
    formatAddress(d) {
      const p = [d.province, d.city, d.district, d.detail_address].filter(Boolean).join(' ')
      return p || '—'
    },
    scoreFmt(v) {
      if (v === undefined || v === null) return '—'
      return Number(v).toFixed(2)
    },
    search() {
      const code = (this.traceCode || '').trim()
      if (!code) {
        this.$modal.msgWarning('请输入溯源码')
        return
      }
      this.searched = true
      this.detail = null
      queryTrace(code).then(res => {
        this.detail = res.data
      }).catch(() => {
        this.detail = null
      })
    },
    copyCode(text) {
      if (!text) return
      if (navigator.clipboard) {
        navigator.clipboard.writeText(text).then(() => this.$message.success('已复制'))
      } else {
        this.$message.warning('当前环境不支持一键复制')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/assets/styles/tcm-theme.scss';

.trace-hero .trace-search {
  margin-top: 20px;
  margin-bottom: 0;
}

.trace-hero ::v-deep .el-input__inner {
  background: rgba(255, 255, 255, 0.95);
}

.code-text {
  font-family: Consolas, monospace;
  font-size: 13px;
  word-break: break-all;
  color: #303133;
  margin: 12px 0;
}

.muted {
  color: #909399;
}

.small {
  font-size: 12px;
  margin-top: 6px;
}

.mb0 {
  margin: 8px 0 0;
}

.hint {
  font-size: 12px;
  color: #909399;
  margin-top: 12px;
}

.score-box {
  text-align: center;
  padding: 16px;
  background: #fafafa;
  border-radius: 8px;
  border: 1px solid #d8e2dc;

  .label {
    font-size: 13px;
    color: #606266;
  }

  .num {
    font-size: 26px;
    font-weight: 700;
    color: #2d6a4f;
    margin-top: 8px;
  }
}
</style>
