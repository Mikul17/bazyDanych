"use client";
import { headerStyle } from "@/constants/Styles";
import paletteProvider from "@/constants/color-palette";
import { Box, Card, Container, Typography } from "@mui/material";
import TransactionItem from "./TransactionItem";
import { useEffect, useState } from "react";
import { jwtDecode } from "jwt-decode";
import { Transaction } from "@/constants/Types";




const TransactionList = () => {
  const palette = paletteProvider();
  const [transactions, setTransactions] = useState<Transaction[]>([]);

  const headerStyleMain = {
    ...headerStyle("center"),
    margin: "0.5rem",
  };

  const cardStyle = {
    backgroundColor: palette.primary.main,
    minHeight: "60vh",
    maxHeight: "80vh",
    overflowY: "scroll",
    borderRadius: "1.25rem",
    "&::-webkit-scrollbar": {
        display: "none",
    },
  };

  const getUserId = () : number => {
    const token = sessionStorage.getItem("token");
    if (!token) {
      throw new Error("Token not found");
    }
    const decoded = jwtDecode<{ userId: number }>(token!);
    return decoded.userId;
  }

  const fetchTransactions = async () => {
    const userId = getUserId();
    const url = "http://localhost:8080/api/transactions/all/" + userId;
    try{
        const response = await fetch(url);

        if(!response.ok){
            throw new Error("Error while fetching transactions");
        }
        const data: Transaction[] = await response.json();
        setTransactions(data);
        let transaction = data[0];
        console.log(transaction);
    }catch(error){
        console.error(error);
    }
  }

  useEffect(() => {
    fetchTransactions();
  }, []);

  return (
    <Container maxWidth="sm" sx={{ marginTop: "1.5rem" }}>
      <Card sx={cardStyle}>
        <Box sx={headerStyleMain}>
          <Typography fontWeight={"bold"} variant="h4">Transaction list</Typography>
        </Box>
        <Box
          display={"flex"}
          justifyContent={"center"}
          alignItems={"center"}
          flexDirection={"column"}
        >
            {transactions.map((transaction) => (
                <TransactionItem transaction={transaction} key={transaction.id}/>
            ))}
        </Box>
      </Card>
    </Container>
  );
};

export default TransactionList;
