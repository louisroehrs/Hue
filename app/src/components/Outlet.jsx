import './Outlet.css';
import {useEffect, useState} from "react";

const  Outlet =({
  id, 
  label,
  exclusive, // only report state, cannot switch.
  outletOn,
  onClick
}) => {

  const [pressed, setPressed ] = useState(false);

  useEffect ( () =>
    {
      setPressed(false);
    },
    [outletOn]
  );

  const handleOnClick = (e) => {
    setPressed(true);
    if (exclusive != null || !exclusive) {
      onClick(e);
    }
  }

  return (
    <div class="outlet"  onClick={handleOnClick}>
      <div class="label">{label}</div>
      <div class={exclusive ? "lockedOut": ( outletOn ? 'switchOn': 'switchOff')} >{exclusive ? "Locked out" : (outletOn ? "Shutdown" : "Energize")}</div>
      <div class={outletOn ? 'poweredOn': 'poweredOff'} >{outletOn?"On": "Off"}</div>
    </div>
  );
}

export default Outlet;
