<template>
  <div class="app-container tcm-page">
    <div class="tcm-hero">
      <h2><i class="el-icon-s-home" /> 中草药溯源管理系统</h2>
      <p>
        中药材从种植到加工的全流程溯源，为每一份中草药产品建立唯一的“数字化身份档案”，实现从种子到终端的全流程追溯。
      </p>
    </div>

    <el-row :gutter="16" style="margin-bottom: 16px;">
      <el-col :xs="24" :sm="8">
        <div class="tcm-stat-card">
          <div class="icon-wrap"><i class="el-icon-location-outline" /></div>
          <div class="label">种植基地数量</div>
          <div class="value">{{ stats.baseCount }}</div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="8">
        <div class="tcm-stat-card">
          <div class="icon-wrap"><i class="el-icon-goods" /></div>
          <div class="label">中草药产品数量</div>
          <div class="value">{{ stats.productCount }}</div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="8">
        <div class="tcm-stat-card">
          <div class="icon-wrap"><i class="el-icon-document" /></div>
          <div class="label">批次数量</div>
          <div class="value">{{ stats.batchCount }}</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="14">
        <el-card shadow="never" class="tcm-trace-card">
          <div slot="header">核心业务管理</div>
          <el-row :gutter="12">
            <el-col :span="12">
              <router-link class="tcm-quick-link" to="/tcm/base"><i class="el-icon-office-building" />种植基地管理</router-link>
              <router-link class="tcm-quick-link" to="/tcm/product"><i class="el-icon-collection-tag" />中草药产品管理</router-link>
              <router-link class="tcm-quick-link" to="/tcm/batch"><i class="el-icon-files" />批次管理</router-link>
            </el-col>
            <el-col :span="12">
              <router-link class="tcm-quick-link" to="/tcm/process"><i class="el-icon-time" />种植加工管理</router-link>
              <router-link class="tcm-quick-link" to="/tcm/trace"><i class="el-icon-search" />溯源查询</router-link>
              <router-link class="tcm-quick-link" to="/tcm/review"><i class="el-icon-chat-dot-round" />评价管理</router-link>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="10">
        <el-card shadow="never" class="tcm-trace-card">
          <div slot="header">标准溯源全流程</div>
          <div class="tcm-flow-hint">
            种子 → 种植 → 施肥 → 用药 → 采收 → 初加工 → 包装，每一个环节都有详细记录，生成唯一溯源码，支持扫描查询。
          </div>
          <el-steps :active="8" finish-status="success" align-center style="margin-top: 16px;">
            <el-step title="种子" />
            <el-step title="种植" />
            <el-step title="施肥" />
            <el-step title="用药" />
            <el-step title="采收" />
            <el-step title="初加工" />
            <el-step title="加工" />
            <el-step title="包装" />
          </el-steps>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { dashboardSummary } from '@/api/tcm/traceability'

export default {
  name: 'TcmDashboard',
  data() {
    return {
      stats: { baseCount: 0, productCount: 0, batchCount: 0 }
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      dashboardSummary().then(res => {
        const d = res.data || {}
        this.stats = {
          baseCount: d.baseCount || 0,
          productCount: d.productCount || 0,
          batchCount: d.batchCount || 0
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/assets/styles/tcm-theme.scss';
</style>
