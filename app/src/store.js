import { configureStore } from  '@reduxjs/toolkit'

import hueBridgeReducer  from './features/huebridge/hueBridgeSlice'
import outletReducer from './features/outlets/outletsSlice'
import chairReducer from "./features/chairs/chairSlice";


export default configureStore ( {
    reducer: {
        hueBridge: hueBridgeReducer,
        outlets : outletReducer,
        chairs : chairReducer
    }
})

