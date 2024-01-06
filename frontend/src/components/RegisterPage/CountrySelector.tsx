import React from 'react';
import { FormControl, InputLabel, MenuItem, Select, SelectChangeEvent } from '@mui/material';
import {getData} from "country-list"
import paletteProvider from '@/constants/color-palette';
import { inputStyle } from '@/constants/Styles';


interface Country {
  code: string;
  name: string;
}

interface CountrySelectorProps {
    onChange: (event: SelectChangeEvent) => void
    value: string;
    margin?: string;
}


const CountrySelector = (props:CountrySelectorProps) => {
  const palette = paletteProvider();
  const countries: Country[] = getData();


  const inputLabelStyle = {
    color: palette.text.secondary,
    border: palette.text.secondary,
    margin: props.margin? props.margin : 0,
    "&.Mui-focused": {
      color: palette.text.secondary,
      border: palette.text.secondary,
    },
  };

  const updateInputStyle = () => {
    return {
      ...inputStyle,
      margin: props.margin? props.margin : 0,
    }
  }

  return (
    <FormControl fullWidth>
      <InputLabel sx={inputLabelStyle}>Country</InputLabel>
      <Select
        defaultValue=""
        value={props.value}
        onChange={props.onChange}
        label="Country"
        sx={updateInputStyle}
      >
        {countries.map((country: Country) => (
          <MenuItem key={country.code} value={country.code}>
            {country.name} ({country.code})
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  );
}

export default CountrySelector;