/** 中草药溯源环节展示：与后端 process_type 约定一致 */
export const PROCESS_STAGE_ORDER = [
  'seed', 'plant', 'irrigate', 'fertilize', 'weed', 'harvest', 'process', 'package'
]

export const PROCESS_STAGE_LABELS = {
  seed: '种子/种苗',
  plant: '种植',
  irrigate: '灌溉',
  fertilize: '施肥',
  weed: '除草',
  harvest: '采收',
  process: '初加工',
  package: '包装'
}

export function getProcessTypeLabel(type) {
  if (!type) return '未知'
  return PROCESS_STAGE_LABELS[type] || type
}
