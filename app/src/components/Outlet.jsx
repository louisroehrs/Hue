import './Outlet.css';

const  Outlet =({
  id, 
  label,
  callForOn,
  outletOn,
  onClick
}) => {

  return (
    <div class="outlet"  onClick={onClick}>
      <div class="label">{label}</div>
      <div class={callForOn ? 'switchOn': 'switchOff'} >{callForOn}</div>
      <div class={outletOn ? 'poweredOn': 'poweredOff'} >{outletOn?"On": "Off"}</div>
    </div>
  );
}

export default Outlet;
