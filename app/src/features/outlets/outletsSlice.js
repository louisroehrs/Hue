import {createSlice} from '@reduxjs/toolkit'

const initialState = {
  outlets: [
    { id: 0, name: '1', callForOn: false, outletOn: false, exclusive: true},
    { id: 1, name: '2', callForOn: false, outletOn: false, exclusive: true},
    { id: 2, name: '3', callForOn: false, outletOn: false, exclusive: true},
    { id: 3, name: '4', callForOn: false, outletOn: false, exclusive: false},
    { id: 4, name: '5', callForOn: false, outletOn: false, exclusive: false},
    { id: 5, name: '6', callForOn: false, outletOn: false, exclusive: false},
    { id: 6, name: '7', callForOn: false, outletOn: false, exclusive: false},
    { id: 7, name: '8', callForOn: false, outletOn: false, exclusive: false}
  ],
  device: { poweredOn: false,
          },
  outletsLoading : false,
  error: null,
}

const outletsSlice  = createSlice ({
  name: 'outlets',
  initialState,
  reducers: {
    outletsLoading: (state,action) => {
      state.outletsLoading = true;
    },
    outletsFetched: (state,action) => {
      state.outlets.map( outlet => {
        outlet.outletOn = action.payload[outlet.id].physical_state;
        outlet.name = action.payload[outlet.id].name;
        return outlet})
      state.outletsLoading = false;
    },

    // payload is an outlet
    outletToggledCallForOn: (state,action) => {
      state.outlets[action.payload.id].callForOn = !action.payload.callForOn;
      state.outletsLoading = false;
    },

    outletsFetchedError: (state,action) => {
      state.outletsLoading = false;
      state.error = action.payload;
    },

    requestedChairsFetched: (state,action) => {
      state.outlets[0].callForOn = action.payload["chairRequest1"];
      state.outlets[1].callForOn = action.payload["chairRequest2"];
      state.outlets[2].callForOn = action.payload["chairRequest3"];
    },

  }
})


export const {
  outletsLoading,
  outletsFetched,
  outletsFetchedError,
  outletToggledCallForOn,
  requestedChairsFetched
} = outletsSlice.actions

export default outletsSlice.reducer
