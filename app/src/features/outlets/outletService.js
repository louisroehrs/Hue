import {
  outletsLoading,
  outletsFetched,
  outletsFetchedError,
  outletToggledCallForOn,
  requestedChairsFetched
} from './outletsSlice'
import {chairsFetched, chairsFetchedError, chairsLoading} from "../chairs/chairSlice";

const outletService = (dispatch)=> {

  const baseUrl = '';

  let outletHeaders= new Headers();
  outletHeaders.append('accept','application/json');
  outletHeaders.append('X-CSRF','x');
  outletHeaders.append('Content-type','application/x-www-form-urlencoded');

  const chairIds = ["One","Two","Three","Four","Five","Six","Seven","Eight"];

  const fetchChairs = async () => {
    dispatch(chairsLoading());
    const response = await fetch(
      baseUrl
      + '/graphql?query='
      + encodeURI('{query: getChairs { id name requested poweredOn}}'),
      {headers: outletHeaders}
    );

    if (response.ok) {
      const json = await response.json();
      dispatch(chairsFetched(json));
    } else {
      dispatch(chairsFetchedError("error"));
    }
  }

  const fetchOutlets= async () => {
    const response = await fetch(
      baseUrl
      + '/graphql?query='
    + encodeURI( '{query: getOutlets { id name physical_state}}'),
      {headers:outletHeaders}
      );
    if (response.ok) {
      const json = await response.json();
      dispatch(outletsFetched(json));
    } else {
      dispatch(outletsFetchedError({error:"error"}));
    }
  }

  //curl -H "Accept: application/json" --digest \ 'http://admin:1234@192.168.0.100/restapi/relay/outlets/=0,1,4/state/'

  const toggleCallForOn = (outlet) => {
    let scriptName = '';
    if (outlet.callForOn) {
      scriptName = "chairOff" + chairIds[outlet.id];
    } else {
      scriptName = "requestChair" + chairIds[outlet.id];
    }
    callScript(scriptName,outlet)
  }

  const callScript = async (scriptName,outlet) => {
    const response = await fetch(
      'http://'
      + 'ipAddress'
      + '/script.cgi', {
      method: 'POST', 
      headers: outletHeaders,
      body: "start=Start&user_function=" + scriptName,
    });
    if (response.ok) {
      dispatch(outletToggledCallForOn(outlet))
    } else {
      console.error('ErrorToggle:', "error in callScript");
    };
  }

  const  getRequestedChairs = async () => {
// {"chairRequest1":true,"chairRequest3":false,"chairRequest2":true,"bunny":"I'm fluffy"}
    const response = await  fetch('http://' + 'ipAddress' + '/restapi/script/variables/', {
      method: 'GET', // or 'PUT'
      headers: outletHeaders
    })
    if (response.ok) {
      const data = await response.json();
      dispatch(requestedChairsFetched(data));
    }
  }

  const setOutlet = async (outlet,on) => {
    const response = await fetch(
      'http://'
      + 'ipAddress'
      + '/restapi/relay/outlets/='
      + outlet
      +'/state/',
      {
        method: 'PUT', // or 'PUT'
        headers: outletHeaders,
        body: "value=" + on,
      }
    )
    if (response.ok) {
      console.log('Success:');
    } else {
      console.error('Error:', "error in setOutlet");
    }
  }
  
  return {
    toggleCallForOn: toggleCallForOn,
    fetchOutlets: fetchOutlets,
    setOutlet: setOutlet,
    getRequestedChairs: getRequestedChairs
  }
}

export default outletService;
