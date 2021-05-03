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
        outlet.outletOn = action.payload.data.query[outlet.id].physical_state;
        outlet.name = action.payload.data.query[outlet.id].name;
        return outlet})
      state.outletsLoading = false;
    },

    // payload is nada
    outletSet: (state,action) => {
      state.outletsLoading = false;
    },

    outletsFetchedError: (state,action) => {
      state.outletsLoading = false;
      state.error = action.payload;
    },
  }
})


export const {
  outletsLoading,
  outletsFetched,
  outletsFetchedError,
  outletSet,
} = outletsSlice.actions

export default outletsSlice.reducer
