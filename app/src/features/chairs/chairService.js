import {
  chairsFetched,
  chairsFetchedError,
  chairsLoading,
  chairRequested
} from "./chairSlice";



const chairService = (dispatch)=> {
  const baseUrl = '';

  let headers = new Headers();
  headers.append('accept', 'application/json')
  headers.append('Content-Type','application/json');

  const fetchChairs = async () => {
    dispatch(chairsLoading());
    const response = await fetch(
      baseUrl
      + '/graphql?query='
      + encodeURI('{query: getChairs { id name requested poweredOn}}'),
      {headers: headers}
    );

    if (response.ok) {
      const json = await response.json();
      dispatch(chairsFetched(json));
    } else {
      dispatch(chairsFetchedError("error"));
    }
  }

  const requestChair = async (id, on) => {
    const response = await fetch(
      baseUrl
      + '/graphql',
      {
        headers: headers,
        body: `{"query":"mutation { requestChairOn (id:${id}, on: ` +
          (on ? "true" : "false") +
          ') {id name requested poweredOn}}"}',
        method: "POST"
      })
    if (response.ok) {
      const json = await response.json();
      dispatch(chairRequested(json));
    } else {
      dispatch(chairsFetchedError({error: "error"}));
    }
  }


  return {
    fetchChairs: fetchChairs,
    requestChair : requestChair
  }

}

export default chairService;

