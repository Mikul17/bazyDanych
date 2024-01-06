import { Box, Button, CardContent, Grid, SelectChangeEvent, TextField, Typography } from "@mui/material";
import CountrySelector from "../RegisterPage/CountrySelector";
import { buttonStyle, headerStyle, inputStyle } from "@/constants/Styles";
import paletteProvider from "@/constants/color-palette";
import { Address } from "@/constants/Types";

interface AddressDetailsProps {
    currentAddress: Address;
    handleAddressChange: (field: keyof Address, value: string) => void;
    handleCountryChange: (event: SelectChangeEvent) => void;
    handleRestrictedInputChange: (
      field: "zipCode" | "houseNumber" | "streetNumber",
      event: React.ChangeEvent<HTMLInputElement>
    ) => void;
    handleSaveChanges: () => void;
    handleDiscardChanges: () => void;
    hasChanges: boolean;
}


const AddressDetails = (props:AddressDetailsProps) => {
    const palette = paletteProvider();

    const innerHeaderStyle = {
        ...headerStyle("center"),
        margin: 0,
      };

    return (
        <>
        <CardContent sx={{ padding: "0 1rem" }}>
          <Box sx={innerHeaderStyle}>
            <Typography variant="h5" fontWeight={"bold"}>
              Address
            </Typography>
          </Box>
          <Grid container spacing={2}>
            <Grid item xs={4}>
              <CountrySelector
                margin="1rem 0"
                onChange={props.handleCountryChange}
                value={props.currentAddress.country}
              />
            </Grid>
            <Grid item xs={8}>
              <TextField
                label="City"
                variant="outlined"
                margin="normal"
                fullWidth
                value={props.currentAddress.city}
                onChange={(e) =>
                  props.handleAddressChange("city", e.target.value)
                }
                sx={inputStyle}
                inputProps={{ maxLength: 50, pattern: "[a-zA-Z ]*" }}
                InputLabelProps={{
                  sx: {
                    "&.Mui-focused": {
                      color: palette.text.secondary,
                    },
                  },
                }}
              />
            </Grid>
          </Grid>
          <TextField
            label="Street"
            variant="outlined"
            margin="normal"
            fullWidth
            value={props.currentAddress.street}
            onChange={(e) =>
              props.handleAddressChange("street", e.target.value)
            }
            sx={inputStyle}
            inputProps={{ maxLength: 50 }}
            InputLabelProps={{
              sx: {
                "&.Mui-focused": {
                  color: palette.text.secondary,
                },
              },
            }}
          />
          <Grid container spacing={2}>
            <Grid item xs={4}>
              <TextField
                label="Street number"
                variant="outlined"
                margin="normal"
                fullWidth
                value={props.currentAddress.streetNumber}
                onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                  props.handleRestrictedInputChange("streetNumber", e)
                }
                sx={inputStyle}
                inputProps={{ maxLength: 5 }}
                InputLabelProps={{
                  sx: {
                    "&.Mui-focused": {
                      color: palette.text.secondary,
                    },
                  },
                }}
              />
            </Grid>
            <Grid item xs={4}>
              <TextField
                label="House number"
                variant="outlined"
                margin="normal"
                fullWidth
                value={props.currentAddress.houseNumber}
                onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                  props.handleRestrictedInputChange("houseNumber", e)
                }
                sx={inputStyle}
                inputProps={{ maxLength: 5 }}
                InputLabelProps={{
                  sx: {
                    "&.Mui-focused": {
                      color: palette.text.secondary,
                    },
                  },
                }}
              />
            </Grid>
            <Grid item xs={4}>
              <TextField
                label="Zip-code"
                variant="outlined"
                margin="normal"
                fullWidth
                value={props.currentAddress.zipCode}
                onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                  props.handleRestrictedInputChange("zipCode", e)
                }
                sx={inputStyle}
                inputProps={{ maxLength: 6, pattern: "[0-9a-zA-Z\\-/]*" }}
                InputLabelProps={{
                  sx: {
                    "&.Mui-focused": {
                      color: palette.text.secondary,
                    },
                  },
                }}
              />
            </Grid>
          </Grid>
          <Box
            display={"flex"}
            justifyContent={"center"}
            alignItems={"center"}
            marginTop={"1rem"}
          >
            <Button
              variant="contained"
              disabled={!props.hasChanges}
              onClick={props.handleSaveChanges}
              sx={{
                ...buttonStyle(palette.secondary.main, "60%"),
                marginRight: "2rem",
              }}
            >
              Update address
            </Button>
            <Button
              onClick={props.handleDiscardChanges}
              disabled={!props.hasChanges}
              variant="contained"
              color="secondary"
              sx={buttonStyle(palette.error.main, "50%")}
            >
              Discard
            </Button>
          </Box>
        </CardContent>
        </>
    );
};


export default AddressDetails;