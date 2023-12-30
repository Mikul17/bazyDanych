"use client"

import paletteProvider from "@/constants/color-palette";
import { Popper } from "@mui/base";
import { Button, Fade, Paper, Popover, Typography } from "@mui/material";
import { Box } from "@mui/system"
import { main } from "@popperjs/core";
import PopupState, { bindToggle, bindPopper, InjectedProps } from 'material-ui-popup-state';
import { bindPopover, bindTrigger, usePopupState } from "material-ui-popup-state/hooks";


interface BalanceProps {
    balance: number;
}




const BalanceDropdown = (props:BalanceProps) => {
    const palette = paletteProvider();
    const popupState = usePopupState({
        variant: 'popover',
        popupId: 'balancePopup'
    })
    

    return (
        <div>
        <Button sx={{background:palette.primary.main, color:palette.text.secondary, borderRadius:"1rem", padding:"0.5rem 2rem", textTransform:"none"}} {...bindTrigger(popupState)}>
          <Typography>{props.balance} zł</Typography>
        </Button>
        <Popover


          {...bindPopover(popupState)}
          anchorOrigin={{
            vertical: 'bottom',
            horizontal: 'left',
          }}
          transformOrigin={{
            vertical: 'top',
            horizontal: 'left',
          }}
        >
            <Box sx={{display:"flex", justifyContent:"center", alignItems:"center", flexDirection:"column"}}>
                <Button>Deposit/Withdraw</Button>
                <Button>Transaction list</Button>
            </Box>
        </Popover>
      </div>
    )

}

export default BalanceDropdown; 