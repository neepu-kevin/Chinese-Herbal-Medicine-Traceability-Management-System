$ErrorActionPreference = 'Stop'
$baseUrl = 'http://127.0.0.1:8080'

# Avoid hardcoding credentials in an open-source repo.
$entUser = if ($env:TCM_ENT_USER) { $env:TCM_ENT_USER } else { 'tcm_enterprise' }
$entPwd = $env:TCM_ENT_PWD
if (-not $entPwd) { throw 'Please set env var TCM_ENT_PWD for enterprise login password.' }

$buyerUser = if ($env:TCM_BUYER_USER) { $env:TCM_BUYER_USER } else { 'tcm_buyer' }
$buyerPwd = $env:TCM_BUYER_PWD
if (-not $buyerPwd) { throw 'Please set env var TCM_BUYER_PWD for buyer login password.' }

$loginBody = "{`"username`":`"$entUser`",`"password`":`"$entPwd`",`"code`":`"`",`"uuid`":`"`"}"
$login = Invoke-RestMethod -Uri "$baseUrl/login" -Method Post -ContentType 'application/json' -Body $loginBody
$token = $login.token
if (-not $token) { throw 'No token from login' }
$headers = @{ Authorization = "Bearer $token" }

$r1 = Invoke-RestMethod -Uri "$baseUrl/tcm/base" -Method Post -Headers $headers -ContentType 'application/json' -Body '{"baseName":"Demo Base","detailAddress":"Kunming","areaSize":120}'
Write-Host "base add:" ($r1 | ConvertTo-Json -Compress)

$list = Invoke-RestMethod -Uri "$baseUrl/tcm/base/list" -Method Get -Headers $headers
$baseId = $list.rows[0].base_id
Write-Host "base_id:" $baseId

$pc = "P-DEMO-" + (Get-Date -Format 'yyyyMMddHHmmss')
$r2 = Invoke-RestMethod -Uri "$baseUrl/tcm/product" -Method Post -Headers $headers -ContentType 'application/json' -Body "{`"productCode`":`"$pc`",`"productName`":`"Demo Herb`",`"originPlace`":`"Yunnan`",`"stockQuantity`":100}"
Write-Host "product add:" ($r2 | ConvertTo-Json -Compress)

$plist = Invoke-RestMethod -Uri "$baseUrl/tcm/product/list" -Method Get -Headers $headers
$productId = $plist.rows[0].product_id
Write-Host "product_id:" $productId

$batchBody = "{`"baseId`":$baseId,`"productId`":$productId,`"batchNo`":`"B-DEMO-$(Get-Date -Format 'yyyyMMddHHmmss')`"}"
$r3 = Invoke-RestMethod -Uri "$baseUrl/tcm/batch" -Method Post -Headers $headers -ContentType 'application/json' -Body $batchBody
Write-Host "batch add:" ($r3 | ConvertTo-Json -Compress)

$blist = Invoke-RestMethod -Uri "$baseUrl/tcm/batch/list" -Method Get -Headers $headers
$batchId = $blist.rows[0].batch_id
Write-Host "batch_id:" $batchId

$procBody = "{`"batchId`":$batchId,`"processType`":`"plant`",`"processContent`":`"planting demo`",`"processTime`":`"2026-04-20 10:00:00`",`"operatorName`":`"tcm_enterprise`"}"
$r4 = Invoke-RestMethod -Uri "$baseUrl/tcm/process" -Method Post -Headers $headers -ContentType 'application/json' -Body $procBody
Write-Host "process add:" ($r4 | ConvertTo-Json -Compress)

$r5 = Invoke-RestMethod -Uri "$baseUrl/tcm/batch/$batchId/publish" -Method Post -Headers $headers
Write-Host "publish:" ($r5 | ConvertTo-Json -Compress)
$traceCode = $r5.data.traceCode
Write-Host "TRACE_CODE:" $traceCode

$pub = Invoke-RestMethod -Uri "$baseUrl/tcm/public/trace/$traceCode" -Method Get
Write-Host "public trace:" ($pub | ConvertTo-Json -Depth 5 -Compress)

$buyerLoginBody = "{`"username`":`"$buyerUser`",`"password`":`"$buyerPwd`",`"code`":`"`",`"uuid`":`"`"}"
$buyerLogin = Invoke-RestMethod -Uri "$baseUrl/login" -Method Post -ContentType 'application/json' -Body $buyerLoginBody
$bh = @{ Authorization = "Bearer $($buyerLogin.token)" }
$revBody = "{`"batchId`":$batchId,`"productScore`":5,`"enterpriseScore`":5,`"reviewContent`":`"good`"}"
$r6 = Invoke-RestMethod -Uri "$baseUrl/tcm/review" -Method Post -Headers $bh -ContentType 'application/json' -Body $revBody
Write-Host "review:" ($r6 | ConvertTo-Json -Compress)

Write-Host "SMOKE_OK"
