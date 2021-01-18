import React from "react";
import './Light.css';
import { HSVtoRGB, RGBtoHSV} from '../algos/hsvandrgb.js';

const Light = ({
    id,
    name,
    on,
    bri,
    hue,
    sat,
    onClick
}) => {

    const {r,g,b} = HSVtoRGB(hue/65535,sat/255,bri/254);
    const styleText = "rgb("+r +","+g+"," +b +")";
    return (
        <>
        <div className ="griditem"
             onClick={onClick}>
            <div className= { on?"on":"off"}>&nbsp;</div>
            <div className="color" style={{backgroundColor:styleText}}>&nbsp;</div>
            <div className="name">{name}</div>
        </div>
        </>
    )
}

export default Light;