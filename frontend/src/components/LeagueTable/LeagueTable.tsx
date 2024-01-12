import { Team } from "@/constants/Types";
import paletteProvider from "@/constants/color-palette";
import { Box, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@mui/material";


interface LeagueTableProps {
    teams: Team[];
};

const LeagueTable = (props:LeagueTableProps) => {

    const palette= paletteProvider();

    const tableStyle={
        marginTop: "1rem",
        backgroundColor: palette.primary.main,
        borderRadius: "1rem",
        maxheight: "75vh",
    }

    const tableHeaderStyle={
        backgroundColor: palette.primary.light,
        color: palette.text.secondary,
        textAlign: "center",
        "&::after": {
            content: '""',
            position: "absolute",
            right: 0,
            top: "20%",
            bottom: "20%",
            width: "2px",
            backgroundColor: palette.text.secondary,
            borderRadius: "1rem",
          },
          "&:not(:last-child)::after": {
            display: "block",
          },
          "&:last-child::after": {
            display: "none",
          }
    }

    const cellStyle = {
        textAlign: 'center',
        verticalAlign: 'middle',
        position: "relative",
        color: palette.text.secondary,
        "&::after": {
          content: '""',
          position: "absolute",
          right: 0,
          top: "20%",
          bottom: "20%",
          width: "2px",
          backgroundColor: palette.text.secondary,
          borderRadius: "1rem",
        },
        "&:not(:last-child)::after": {
          display: "block",
        },
        "&:last-child::after": {
          display: "none",
        }
      };

    const calculateGoalDifference = (goalsScored: number, goalsConceded: number) => {
        return goalsScored - goalsConceded;
    }

    return (
        <TableContainer sx={tableStyle}>
            <Table>
                <TableHead >
                    <TableRow>
                        <TableCell sx={{...cellStyle, color:palette.text.primary, backgroundColor:palette.primary.light}}>Position</TableCell>
                        <TableCell sx={{...cellStyle, color:palette.text.primary, backgroundColor:palette.primary.light}}>Team</TableCell>
                        <TableCell sx={{...cellStyle, color:palette.text.primary, backgroundColor:palette.primary.light}}>W</TableCell>
                        <TableCell sx={{...cellStyle, color:palette.text.primary, backgroundColor:palette.primary.light}}>D</TableCell>
                        <TableCell sx={{...cellStyle, color:palette.text.primary, backgroundColor:palette.primary.light}}>L</TableCell>
                        <TableCell sx={{...cellStyle, color:palette.text.primary, backgroundColor:palette.primary.light}}>GS</TableCell>
                        <TableCell sx={{...cellStyle, color:palette.text.primary, backgroundColor:palette.primary.light}}>GC</TableCell>
                        <TableCell sx={{...cellStyle, color:palette.text.primary, backgroundColor:palette.primary.light}}>GD</TableCell>
                        <TableCell sx={{...cellStyle, color:palette.text.primary, backgroundColor:palette.primary.light}}>PTS</TableCell>
                    </TableRow>
                    </TableHead>
                    <TableBody>
                        {props.teams.map((team, index) => (
                                <TableRow key={team.id}>
                                <TableCell sx={cellStyle}>{index+1}</TableCell>
                                <TableCell sx={cellStyle}>{team.teamName}</TableCell>
                                <TableCell sx={cellStyle}>{team.wins}</TableCell>
                                <TableCell sx={cellStyle}>{team.draws}</TableCell>
                                <TableCell sx={cellStyle}>{team.loses}</TableCell>
                                <TableCell sx={cellStyle}>{team.goalsScored}</TableCell>
                                <TableCell sx={cellStyle}>{team.goalsConceded}</TableCell>
                                <TableCell sx={cellStyle}>{calculateGoalDifference(team.goalsScored, team.goalsConceded)}</TableCell>
                                <TableCell sx={{...cellStyle, borderRight:"none"}}>{team.leaguePoints}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
            </Table>
        </TableContainer>
    );
};

export default LeagueTable;