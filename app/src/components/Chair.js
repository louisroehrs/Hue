import {React, useState} from "react";
import './Outlet.css';

const Chair = ({

                 id,
                 name,
                 poweredOn,
                 requested,
                 onClick
               }) => {

  return (
    <div key={id}
         class="outlet" onClick={onClick}>
      <div class="label">{name}</div>
      <div class={requested ? 'switchOn' : 'switchOff'}>{requested?"Requested":""}</div>
      <div class={poweredOn ? 'poweredOn' : 'poweredOff'}>{poweredOn ? "On" : "Off"}</div>
    </div>
  )
}
export default Chair;