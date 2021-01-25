import {
  lightsFetched,
  lightsFetchedError,
  lightsLoading,
  sensorsLoading,
  sensorsFetched,
  sensorsFetchedError
} from "./hueBridgeSlice";

const baseUrl = ''; 

const hueBridgeService = (dispatch)=> {
    let headers = new Headers();
    headers.append('accept', 'application/json')
    headers.append('Content-Type','application/json');
//    headers.append('Authorization', 'Basic ' + btoa("admin:1234"));

  const fetchLights = () => {
    dispatch(lightsLoading());
    fetch(baseUrl + '/graphql?query=' + encodeURI('{query: getAllLights { id name state {on, hue, bri, sat}}}'),
      {headers: headers}
    )
      .then(res => res.json())
      .then(res => {
        if (res.error) {
          throw(res.error);
        }
        dispatch(lightsFetched(res));

        return res;
      })
      .catch(error => {
        dispatch(lightsFetchedError("error"));
      })
  }

  const turnLightOn = (id, on) => {
    debugger;
    fetch(baseUrl + '/graphql',
      {
        headers: headers,
        body: `{"query":"mutation { turnLightOn (id:${id}, on: ` +
          (on?"true":"false") +
          ') {id name state {on bri hue sat}}}"}',
        method: "POST"
      })
      .then (res => res.json())
      .then (res => {
        if (res.error) {
          throw (res.error);
        }
        res.data.query = res.data.turnLightOn;
        dispatch(lightsFetched(res));
        return res;
      })
      .catch (error => {
        dispatch(lightsFetchedError(error));
      })
  }

  const setLightColor = (id, color) => {
    debugger;
    fetch(baseUrl + '/graphql',
      {
        headers: headers,
        body: `{"query":"mutation { setLightColor (id:${id}`
          + ', bri:' + color.bri
          + ', hue:' + color.hue
          + ', sat:' + color.sat
          + ') {id name state {on bri hue sat}}}"}',
        method: "POST"
      })
      .then(res => res.json())
      .then(res => {
        if (res.error) {
          throw (res.error);
        }
        res.data.query = res.data.setLightColor;
        dispatch(lightsFetched(res));
        return res;
      })
      .catch(error => {
        dispatch(lightsFetchedError(error));
      })
  }
  const fetchSensors = () => {
    dispatch(sensorsLoading());
    fetch(baseUrl + '/graphql?query=' + encodeURI('{query: getAllSensors { id name modelid type manufacturername config {battery}}}'),
      {headers: headers}
    )
      .then(res => res.json())
      .then(res => {
        if (res.error) {
          throw(res.error);
        }
        dispatch(sensorsFetched(res));

        return res;
      })
      .catch(error => {
        dispatch(sensorsFetchedError("error"));
      })
  }


  return {
    fetchLights: fetchLights,
    turnLightOn: turnLightOn,
    setLightColor: setLightColor,
    fetchSensors: fetchSensors
  }
}

export default hueBridgeService;

