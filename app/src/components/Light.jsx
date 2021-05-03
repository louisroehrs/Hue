import {React, useState} from "react";
import './Light.css';
import Drawer from './Drawer';
import { RgbColorPicker } from "react-colorful";
import "react-colorful/dist/index.css";


import { HSVtoRGB, RGBtoHSV} from '../algos/hsvandrgb.js';

const Light = ({
                 id,
                 name,
                 on,
                 bri,
                 hue,
                 sat,
                 onClick,
                 onColorChange:onColorChangeProp
               }) => {

  const {r,g,b} = HSVtoRGB(hue/65535,sat/255,bri/254);
  const styleText = "rgb("+r +","+g+"," +b +")";

  const [drawerOpen, setDrawerOpen] = useState(false);
  //const [color, setColor] = useState({hue:hue,bri:bri,sat:sat});

  const openColor = (event) => {
    setDrawerOpen(!drawerOpen);
    event.stopPropagation();
  }

  const onColorChange = (color) => {
//    setColor({hue:color.h,bri:color.bri,sat:color.s});
    onColorChangeProp(color);
  }

  return (
    <>
      <div className ="griditem"
           onClick={onClick}>
        <div className="drawerOpener" onClick={openColor} />
        <div className={"griditemgap"}/>
        {/* TODO those non apple browsers will not show emojis */}
        {on &&  <div className="on">☀️</div>}

        {!on && <div className="off">&nbsp;️</div>}

        <div className={"griditemgap"}/>
        <div className="color" onClick={openColor} style={{backgroundColor:on?styleText:"black"}}>&nbsp;</div>
        <div className={"griditemgap"}/>
        <div className="name">{name}</div>
        <Drawer open={drawerOpen}>
          <div style={{color:"white"}}>
          <div className={"number"}>{hue}</div><div className={"number"}>{sat}</div><div className={"number"}>{bri}</div>
          </div>
          <div>&nbsp;</div>
          <div>&nbsp;</div>
          <RgbColorPicker className={"colorPicker"}
            color={{r:r,g:g,b:b}}
            onChange={onColorChange}
          />
        </Drawer>
      </div>
    </>
  )
}

export default Light;