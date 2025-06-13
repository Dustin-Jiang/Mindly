package top.tsukino.mindly.feature

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import top.tsukino.mindly.api.ApiConfig
import top.tsukino.mindly.api.MindlyApi
import top.tsukino.mindly.data.repo.base.ProviderRepo
import top.tsukino.mindly.feature.common.helper.withScope
import javax.inject.Inject

@HiltViewModel
class MainAppViewModel @Inject constructor(
    private val api: MindlyApi,
    private val providerRepo: ProviderRepo
) : ViewModel() {
    init {
        withScope {
            viewModelScope.launch(Dispatchers.IO) {
                Log.d("MainAppViewModel", "开始收集 providers")
                providerRepo.getProviders().collect { providersList ->
                    val providers = providersList
                    Log.d("MainAppViewModel", "收到 providers: 共 ${providers.size} 个")
                    
                    providers.forEachIndexed { index, provider ->
                        try {
                            Log.d("MainAppViewModel", "处理第 ${index + 1} 个 provider: ${provider.name}, ${provider.host}")
                            
                            // 添加provider到API
                            api.addProvider(
                                provider.name,
                                ApiConfig(
                                    token = provider.token,
                                    host = provider.host
                                )
                            )
                            Log.d("MainAppViewModel", "成功添加 provider: ${provider.name} 到API")
                            
                            // 更新provider模型
                            viewModelScope.launch(Dispatchers.IO) {
                                providerRepo.updateProviderModels(provider)
                                Log.d("MainAppViewModel", "成功更新 provider: ${provider.name} 的模型")
                            }
                        } catch (e: Exception) {
                            Log.e("MainAppViewModel", "处理 provider ${provider.name} 时出错: ${e.message}", e)
                            // 继续处理下一个provider，而不是中断整个循环
                        }
                    }
                    
                    Log.d("MainAppViewModel", "所有 providers 处理完成")
                }
            }
        }
    }
}
