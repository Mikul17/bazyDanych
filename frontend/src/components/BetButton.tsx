import paletteProvider from '@/constants/color-palette';
import { Box, Button, Typography } from '@mui/material';
import React from 'react';

interface BetButtonProps {
    betName: string;
    betOdds: number;
}

const BetButton= (props : BetButtonProps) => {
    const palette = paletteProvider();
    const [isAdded, setIsAdded] = React.useState<boolean>(false);

    const typographyStyle = {
        fontSize: "0.75rem",
        color: palette.text.primary,
    };

    const buttonStyle = {
        borderRadius: "1rem",
        backgroundColor: isAdded?palette.primary.dark:palette.text.light,
        margin: "0 0.5rem",
        "&:hover": {
            backgroundColor: palette.primary.main,
        },
    }

    const onButtonClick = () => {
        setIsAdded(!isAdded);
    }

    return (
        <Button sx={buttonStyle} onClick={onButtonClick}> 
            <Box display={"flex"} justifyContent={"space-around"} alignContent={"center"} flexDirection={"column"}>
            <Typography sx={typographyStyle}>{props.betName}</Typography>
            <Typography sx={typographyStyle}>{props.betOdds}</Typography>
            </Box>
        </Button>
    );
};

export default BetButton;
