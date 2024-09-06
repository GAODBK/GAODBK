import 'element-plus/dist/index.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './index.js'
import './index.css'
import ElementPlus from 'element-plus'

const app = createApp(App)

app.use(router)
app.use(ElementPlus)

app.mount('#app')
