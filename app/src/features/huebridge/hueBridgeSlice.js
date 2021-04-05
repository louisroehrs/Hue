import {createSlice} from '@reduxjs/toolkit'

const initialState = {
    hueBridgeLoading : false,
    lights: [],
    sensors:[]
};

const hueBridgeSlice  = createSlice ({
    name: 'hueBridge',
    initialState,
    reducers: {
        lightsLoading: (state,action) => {
            state.hueBridgeLoading = true;
        },
        lightsFetched: (state,action) => {
            debugger;
            state.lights = action.payload.data.query.sort((a,b)=> (a.name> b.name));
            state.hueBridgeLoading = false;
        },
        lightsFetchedError: (state,action) => {
            state.hueBridgeLoading = false;
            state.error = action.payload;
        },

        sensorsLoading: (state,action) => {
            state.hueBridgeLoading = true;
        },
        sensorsFetched: (state,action) => {
            state.sensors = action.payload.data.query.sort((a,b)=> (a.name> b.name));
            state.hueBridgeLoading = false;
        },
        sensorsFetchedError: (state,action) => {
            state.hueBridgeLoading = false;
            state.error = action.payload;
        }
    }
})


export const {
    lightsLoading,
    lightsFetched,
    lightsFetchedError,
    sensorsLoading,
    sensorsFetched,
    sensorsFetchedError
} = hueBridgeSlice.actions

export default hueBridgeSlice.reducer