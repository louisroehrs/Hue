import {createSlice} from '@reduxjs/toolkit'

const initialState = {
    hueBridgeLoading : false,
    lights: []
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
        }
    }
})


export const { lightsLoading, lightsFetched, lightsFetchedError} = hueBridgeSlice.actions

export default hueBridgeSlice.reducer