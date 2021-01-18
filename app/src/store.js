import { configureStore } from  '@reduxjs/toolkit'

import hueBridgeReducer  from './features/huebridge/hueBridgeSlice'


export default configureStore ( {
    reducer: {
        hueBridge: hueBridgeReducer
    }
})

