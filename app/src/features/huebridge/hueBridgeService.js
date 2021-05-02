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

  const fetchLights = async () => {
    dispatch(lightsLoading());
    const response = await fetch(
      baseUrl
      + '/graphql?query='
      + encodeURI('{query: getLights { id name state {on, hue, bri, sat}}}'),
      {headers: headers}
    );

    if (response.ok) {
      const json = await response.json();
      dispatch(lightsFetched(json));
    } else {
      dispatch(lightsFetchedError("error"));
    }
  }

  const turnLightOn = async (id, on) => {
    const response = await fetch(
      baseUrl
      + '/graphql',
      {
        headers: headers,
        body: `{"query":"mutation { turnLightOn (id:${id}, on: ` +
          (on?"true":"false") +
          ') {id name state {on bri hue sat}}}"}',
        method: "POST"
      })
      if (response.ok) {
        const json = await response.json();
        json.data.query = json.data.turnLightOn;
        dispatch(lightsFetched(json));
      } else {
        dispatch(lightsFetchedError({error:"error"}));
      }
  }

  const setLightColor = async (id, color) => {
    const response = await fetch(
      baseUrl
      + '/graphql',
      {
        headers: headers,
        body: `{"query":"mutation { setLightColor (id:${id}`
          + ', bri:' + color.bri
          + ', hue:' + color.hue
          + ', sat:' + color.sat
          + ') {id name state {on bri hue sat}}}"}',
        method: "POST"
      });

    if ( response.ok) {
      const json = await response.json();
      json.data.query = json.data.setLightColor;
      dispatch(lightsFetched(json));
    } else {
      dispatch(lightsFetchedError({error:"error"}));
    }
  }

  const fetchSensors =  async () => {
    dispatch(sensorsLoading());
    const response = await fetch(
      baseUrl
      + '/graphql?query='
      + encodeURI('{query: getSensors { id name modelid type manufacturername config {battery}}}'),
      {headers: headers}
    );
    if (response.ok) {
      const json = await response.json();
      dispatch(sensorsFetched(json));
    } else {
      dispatch(sensorsFetchedError({error:"error"}));
    }
  }

  return {
    fetchLights: fetchLights,
    turnLightOn: turnLightOn,
    setLightColor: setLightColor,
    fetchSensors: fetchSensors
  }
}

export default hueBridgeService;

