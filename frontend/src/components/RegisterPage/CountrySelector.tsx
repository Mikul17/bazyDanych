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
}


const CountrySelector = (props:CountrySelectorProps) => {
  const palette = paletteProvider();
  const countries: Country[] = getData();


  const inputLabelStyle = {
    color: palette.text.secondary,
    border: palette.text.secondary,
    "&.Mui-focused": {
      color: palette.text.secondary,
      border: palette.text.secondary,
    },
  };

  return (
    <FormControl fullWidth>
      <InputLabel sx={inputLabelStyle}>Country</InputLabel>
      <Select
        defaultValue=""
        value={props.value}
        onChange={props.onChange}
        label="Country"
        sx={inputStyle}
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