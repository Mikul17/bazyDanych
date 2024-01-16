import { headerStyle } from "@/constants/Styles";
import { Match, MatchHistory } from "@/constants/Types";
import paletteProvider from "@/constants/color-palette";
import { Box, Card, Modal, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import MatchHistoryItem from "./MatchHistoryItem";

interface MatchHistoryModalProps {
  openModal: boolean;
  handleCloseModal: () => void;
  isHomeTeam: boolean;
  matchId: number;
  teamName: string;
}

const MatchHistoryModal = (props: MatchHistoryModalProps) => {
  const palette = paletteProvider();
  const [matchHistory, setMatchHistory] = useState<MatchHistory[]>([]);


  const fetchMatchHistory = async () => {
    const url = `http://localhost:8080/api/match/history/?matchId=${props.matchId}&isHomeTeam=${props.isHomeTeam}`;
    try{
        const response = await fetch(url);
        if(!response.ok){
            throw new Error("Error fetching match history");
        }else{
            const data = await response.json();
            setMatchHistory(data);
        }
    }catch(error){
        console.error(error);
        return null;
    }
  };

  const modalStyle = {
    position: "absolute" as "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    width: "30%",
    bgcolor: palette.primary.main,
    boxShadow: 24,
    p: "1rem",
    outline: 'none' ,
    borderRadius: "1.25rem",
  };

  const listStyle = {
    width: "100%",
    height: "100%",
    overflow: "auto",
    backgroundColor: palette.primary.light,
    borderRadius: "1.25rem",
  };

  const coloredHeaderStyle = {
    ...headerStyle("center"),
    width: "90%",
    backgroundColor: palette.primary.main,
    color: palette.text.secondary,
  };

  useEffect(() => {
    fetchMatchHistory();
  }, [props.openModal]);

  return (
    <Modal
      open={props.openModal}
      onClose={props.handleCloseModal}
      aria-labelledby="deposit-withdraw-modal"
      aria-describedby="modal-modal-description"
    >
      <Card sx={modalStyle}>
        <Box sx={headerStyle("center")}>
          <Typography fontWeight={"bold"} fontSize={"2rem"} sx={{ color: palette.primary.main }}>
            {props.teamName}
          </Typography>
        </Box>
        <Box display={"flex"} justifyContent={"center"} alignItems={"center"} flexDirection={"column"} sx={listStyle}>
            <Box sx={coloredHeaderStyle}>
                <Typography fontWeight={"bold"} fontSize={"1.5rem"} sx={{ color: palette.text.secondary }}>
                    Recent performance
                </Typography>
            </Box>
            {matchHistory.map((match) => {
                return <MatchHistoryItem isHomeTeam={props.isHomeTeam} match={match} />;
            })}  
        </Box>
      </Card>
    </Modal>
  );
};

export default MatchHistoryModal;
