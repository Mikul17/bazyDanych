import { Bet } from "@/constants/Types";
import { AccessTimeOutlined, CancelOutlined, CheckCircleOutline } from "@mui/icons-material";
import BetItem from "./BetItem";
import { Box } from "@mui/material";



interface BetItemWithIconProps {
    bet: Bet;
}

const BetItemWithIcon = ({bet}: BetItemWithIconProps) => {

    const betStatusIconStyle = {
        fontSize: "2.5rem",
        marginLeft: "2rem",
      };


      const displayBetStatusIcon = () => {
        switch (bet.betStatus) {
          case 0:
            return <AccessTimeOutlined color="warning" sx={betStatusIconStyle}/>;
          case 1:
            return <CheckCircleOutline color="action" sx={betStatusIconStyle}/>;
          case 2:
            return <CancelOutlined color="error" sx={betStatusIconStyle}/>;
          default:
            throw "Invalid bet status";
        }
      };


      return ( 
        <Box display={"flex"} justifyContent={"center"} alignItems={"center"} width={"100%"}>
            <BetItem bet={bet} isDeleteable={false}/>
            {displayBetStatusIcon()}
        </Box>
      )
};

export default BetItemWithIcon;