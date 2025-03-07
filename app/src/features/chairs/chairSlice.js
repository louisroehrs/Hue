import {createSlice} from '@reduxjs/toolkit'

const initialState = {
  chairs: [] ,
//    { id: 0, name: '1', requested: false, poweredOn: false},
//    { id: 1, name: '2', requested: false, poweredOn: false},
//    { id: 2, name: '3', requested: false, poweredOn: false},

  error: null,
}

const chairSlice  = createSlice ({
  name: 'chairs',
  initialState,
  reducers: {
    chairsLoading: (state,action) => {
      state.chairsLoading = true;
    },
    chairsFetched: (state,action) => {
      if (action.payload.data.query) {
        state.chairs = action.payload.data.query.map(chair => {
          chair.name = chair.name;
          chair.requested = chair.requested;
          chair.poweredOn = chair.poweredOn;
          return chair
        })
        state.chairsLoading = false;
      }
    },

    chairRequested: (state,action) => {
      state.chairs= action.payload.data.requestChairOn.map( chair => {
        chair.name = chair.name;
        chair.requested = chair.requested;
        chair.poweredOn = chair.poweredOn;
        return chair})
      state.chairsLoading = false;
    },

    chairsFetchedError: (state,action) => {
      state.chairs= action.payload.data.map( chair => {
        chair.name = chair.name;
        chair.requested = chair.requested;
        chair.poweredOn = chair.poweredOn;
        return chair})
      state.chairsLoading = false;
    }
  }

})


export const {
  chairsLoading,
  chairsFetched,
  chairRequested,
  chairsFetchedError
} = chairSlice.actions

export default chairSlice.reducer;
