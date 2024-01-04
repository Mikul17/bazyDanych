import { alpha } from "@mui/material";
import paletteProvider from "@/constants/color-palette";

const palette = paletteProvider();

export const headerStyle = (justifyContent: string) => ({
  display: "flex",
  justifyContent: justifyContent,
  alignItems: "center",
  color: palette.primary.main,
  backgroundColor: palette.background.default,
  borderRadius: "1.25rem 1.25rem 0.25rem 0.25rem",
  padding: "0.5rem",
  margin: "0.5rem 0rem",
});

export const buttonStyle = (color: string, width?:string) => ({
  width: width?width:"100%",
  margin: "0.5rem 0rem",
  borderRadius: "0.5rem",
  backgroundColor: color,
  fontWeight: "bold",
  color: palette.text.secondary,
  "&:hover": {
    backgroundColor: alpha(color, 0.75),
  },
});

export const inputStyle = {
  ".MuiInputLabel-outlined": {
    color: palette.text.secondary,
  },
  input: { color: palette.text.secondary },
  border: palette.text.secondary,
  "& .MuiOutlinedInput-root": {
    "& fieldset": {
      borderColor: "white",
    },
    ".MuiInputLabel-outlined.Mui-focused": {
      color: palette.primary.main,
    },
    "&:hover fieldset": {
      borderColor: "white",
    },
    "&.Mui-focused fieldset": {
      borderColor: palette.text.secondary,
    },
    "&.Mui-active fieldset": {
      color: palette.text.secondary,
    },
    "& input[type=number]": {
      MozAppearance: "textfield",
    },
    "& input::-webkit-outer-spin-button, & input::-webkit-inner-spin-button": {
      display: "none",
    },
    "& MuiInputLabel.Mui-focused": {
      color: palette.text.secondary,
    },
  },
  ".MuiSelect-outlined": {
    color: palette.text.secondary,
  },
  '.MuiOutlinedInput-notchedOutline': {
      borderColor: palette.text.secondary,
    },
    '&.Mui-focused .MuiOutlinedInput-notchedOutline': {
      borderColor: palette.text.secondary,
    },
    '&:hover .MuiOutlinedInput-notchedOutline': {
      borderColor: palette.text.secondary,
    },
    '.MuiSelect-icon': {
      color: palette.text.secondary, 
    },
};

