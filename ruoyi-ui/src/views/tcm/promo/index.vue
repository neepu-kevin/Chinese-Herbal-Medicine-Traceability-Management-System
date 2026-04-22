<template>
  <div class="app-container tcm-page">
    <div class="tcm-hero">
      <h2><i class="el-icon-shopping-cart-full" /> 草药促销</h2>
      <p>基于产品档案，展示当前推荐草药与促销方案（可用于电商上架前的营销展示与导购）。</p>
    </div>

    <el-row :gutter="16" style="margin-bottom: 16px;">
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="tcm-trace-card">
          <div slot="header">促销规则（示例）</div>
          <div class="promo-rules">
            <div class="rule">
              <div class="title"><i class="el-icon-ticket" /> 新客立减</div>
              <div class="desc">首单满 99 元减 20 元（用于线下门店/线上商城引流）。</div>
            </div>
            <div class="rule">
              <div class="title"><i class="el-icon-present" /> 套餐优惠</div>
              <div class="desc">同产地/同品类产品可打包销售，提升复购与客单价。</div>
            </div>
            <div class="rule">
              <div class="title"><i class="el-icon-collection-tag" /> 溯源推荐</div>
              <div class="desc">支持溯源码查询的产品优先推荐，强化“可追溯、可信赖”的品牌心智。</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="tcm-trace-card">
          <div slot="header">促销看板</div>
          <el-row :gutter="12">
            <el-col :span="8">
              <div class="tcm-stat-card">
                <div class="icon-wrap"><i class="el-icon-goods" /></div>
                <div class="label">在售产品</div>
                <div class="value">{{ products.length }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="tcm-stat-card">
                <div class="icon-wrap"><i class="el-icon-star-on" /></div>
                <div class="label">推荐产品</div>
                <div class="value">{{ featured.length }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="tcm-stat-card">
                <div class="icon-wrap"><i class="el-icon-price-tag" /></div>
                <div class="label">促销方案</div>
                <div class="value">{{ plans.length }}</div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="tcm-trace-card">
      <div slot="header">促销产品推荐</div>

      <el-row :gutter="16" v-loading="loading">
        <el-col v-for="p in featured" :key="p.product_id" :xs="24" :sm="12" :md="8" :lg="6">
          <div class="promo-card">
            <div class="head">
              <div class="name">{{ p.product_name }}</div>
              <el-tag type="danger" size="mini">{{ planBadge(p) }}</el-tag>
            </div>
            <div class="meta">
              <div class="line"><span class="k">产品编码：</span><span class="v">{{ p.product_code }}</span></div>
              <div class="line"><span class="k">产地：</span><span class="v">{{ p.origin_place || '—' }}</span></div>
              <div class="line"><span class="k">库存：</span><span class="v">{{ p.stock_quantity || 0 }}</span></div>
            </div>
            <div class="actions">
              <router-link class="link" to="/tcm/product"><i class="el-icon-edit" /> 去维护产品</router-link>
              <router-link class="link" to="/tcm/batch"><i class="el-icon-document" /> 去建批次</router-link>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-empty v-if="!loading && featured.length === 0" description="暂无产品数据，请先在「产品档案」中新增产品。" />
    </el-card>
  </div>
</template>

<script>
import { listProduct } from '@/api/tcm/traceability'

export default {
  name: 'TcmPromo',
  data() {
    return {
      loading: false,
      products: [],
      plans: [
        { id: 'new', label: '新客立减' },
        { id: 'bundle', label: '套餐优惠' },
        { id: 'trace', label: '溯源推荐' }
      ]
    }
  },
  computed: {
    featured() {
      // 选取库存 > 0 的前 N 个作为推荐；后续可替换为“销量/评分/活动配置”规则。
      const arr = (this.products || []).slice().filter(p => (p.stock_quantity || 0) > 0)
      return arr.slice(0, 8)
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.loading = true
      listProduct().then(res => {
        this.products = (res && res.rows) || []
      }).finally(() => {
        this.loading = false
      })
    },
    planBadge(p) {
      if (!p) return '促销'
      if ((p.stock_quantity || 0) >= 500) return '批量优惠'
      if ((p.origin_place || '').includes('云南')) return '产地推荐'
      return '溯源推荐'
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/assets/styles/tcm-theme.scss';

.promo-rules {
  .rule {
    padding: 12px 0;
    border-bottom: 1px dashed #e5e7eb;

    &:last-child {
      border-bottom: none;
    }

    .title {
      font-weight: 700;
      color: #1b4332;
      margin-bottom: 6px;
    }

    .desc {
      color: #606266;
      line-height: 1.7;
      font-size: 13px;
    }
  }
}

.promo-card {
  border: 1px solid #d8e2dc;
  border-radius: 10px;
  background: #fff;
  padding: 14px 14px 12px;
  margin-bottom: 16px;
  transition: transform 0.15s, box-shadow 0.15s;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 10px 22px rgba(45, 106, 79, 0.10);
  }

  .head {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 10px;
  }

  .name {
    font-weight: 800;
    color: #1b4332;
    font-size: 14px;
    line-height: 1.4;
    padding-right: 10px;
  }

  .meta {
    .line {
      display: flex;
      line-height: 1.7;
      font-size: 12px;
      color: #374151;
      margin: 2px 0;

      .k {
        width: 72px;
        color: #6b7280;
      }

      .v {
        flex: 1;
        word-break: break-all;
      }
    }
  }

  .actions {
    margin-top: 10px;
    display: flex;
    gap: 10px;

    .link {
      flex: 1;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      gap: 6px;
      height: 30px;
      border-radius: 8px;
      background: rgba(64, 145, 108, 0.08);
      color: #2d6a4f;
      text-decoration: none;
      font-size: 12px;
      border: 1px solid rgba(64, 145, 108, 0.18);

      &:hover {
        background: rgba(64, 145, 108, 0.12);
        border-color: rgba(64, 145, 108, 0.28);
      }
    }
  }
}
</style>
