import {React, useState, useEffect} from 'react';
import './Chair.css';

const Chair = ({

                 id,
                 name,
                 poweredOn,
                 requested,
                 onClick
               }) => {

  const [pressed, setPressed ] = useState(false);

  useEffect ( () =>
  {
    setPressed(false);
  },
    [requested]
  );

  const handleOnClick = (e) => {
    setPressed(true);
    onClick(e);
  }

  return (
    <div key={id}
         class="chair" onClick={handleOnClick}>
      <div class="label">{name}
      </div>
      <img width={"100%"} src={name.toLowerCase().includes("couch")?"/apollocouch.png":"/apollochair.png"} />
      <div class={(requested ? 'switchOn' : 'switchOff') + (pressed ? " pressed" :"")}>{pressed?"Working" :requested?"Requested":"Request Heat"}</div>
      <div class={poweredOn ? 'poweredOn' : 'poweredOff'}>{poweredOn ? "On" : "Off"}</div>
    </div>
  )
}
export default Chair;