import {React, useState, useEffect} from 'react';
import './Outlet.css';

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
    <div class="griditem outlet"  onClick={handleOnClick}>
      <div class="name">{label}</div>
      {/* reverse order float right */}
      <div className="griditemgap"></div>
      <div className={outletOn ? 'poweredOn' : 'poweredOff'}>{outletOn ? "On" : "Off"}</div>
      <div className="griditemgap"></div>
      <div class={exclusive ? "lockedOut": (( outletOn ? 'switchOn': 'switchOff') + (pressed ? " pressed" :""))} >{exclusive ? "Locked out" : (outletOn ? "Shutdown" : "Energize")}</div>
      <div className="griditemgap"></div>
    </div>
  );
}

export default Outlet;
