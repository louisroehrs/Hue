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
            console.log('Received lights data:', action.payload.data.query);
            if (!action.payload.data.query) {
                console.error('No lights data in payload');
                return;
            }
            const sortedLights = [...action.payload.data.query].sort((a,b) => {
                if (!a.name || !b.name) {
                    console.error('Missing name property:', a, b);
                    return 0;
                }
                return a.name.localeCompare(b.name);
            });
            console.log('Sorted lights:', sortedLights);
            state.lights = sortedLights;
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
            console.log('Received sensors data:', action.payload.data.query);
            if (!action.payload.data.query) {
                console.error('No sensors data in payload');
                return;
            }
            const sortedSensors = [...action.payload.data.query].sort((a,b) => {
                if (!a.name || !b.name) {
                    console.error('Missing name property:', a, b);
                    return 0;
                }
                return a.name.localeCompare(b.name);
            });
            console.log('Sorted sensors:', sortedSensors);
            state.sensors = sortedSensors;
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