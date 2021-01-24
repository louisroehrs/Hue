import React from "react";

const Drawer = ({
                  id,
                  open,
                  ...props
                 }) => {

  const stopPropagation = (event) => {
    event.stopPropagation();
  }

  return  (
    open ?
      <>
        <div
          className={"drawer"}
          onClick={stopPropagation}>
          {props.children}
        </div>
      </>
      :
      <>
      </>
  )
}

export default Drawer;