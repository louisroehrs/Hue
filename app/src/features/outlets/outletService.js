import {
  outletsLoading,
  outletsFetched,
  outletsFetchedError,
  outletSet
} from './outletsSlice'


const outletService = (dispatch)=> {

  const baseUrl = '';

  let headers= new Headers();
  headers.append('accept','application/json');
  headers.append('Content-type','application/json');

  const fetchOutlets= async () => {
    const response = await fetch(
      baseUrl
      + '/graphql?query='
    + encodeURI( '{query: getOutlets { id name physical_state}}'),
      {headers:headers}
      );
    if (response.ok) {
      const json = await response.json();
      dispatch(outletsFetched(json));
    } else {
      dispatch(outletsFetchedError({error:"error"}));
    }
  }

  const setOutlet = async (outlet,on) => {
    const response = await fetch(
      baseUrl
      + '/graphql',
      {
        body: `{"query":"mutation { turnOutletOn (id:${outlet}, on: ` +
          (on ? "true" : "false") +
          ') {id name physical_state}}"}',
        method: "POST",
        headers: headers,
      }
    )
    if (response.ok) {
      const json = await response.json();
      json.data.query = json.data.turnOutletOn;
      dispatch(outletsFetched(json));
      console.log('Success:');
    } else {
      console.error('Error:', "error in setOutlet");
    }
  }
  
  return {
//   toggleCallForOn: toggleCallForOn,
    fetchOutlets: fetchOutlets,
    setOutlet: setOutlet,
//    getRequestedChairs: getRequestedChairs
  }
}

export default outletService;
