import { Transaction } from "@/constants/Types";
import paletteProvider from "@/constants/color-palette";
import { Box, Paper, Stack, Typography } from "@mui/material";

type TransactionItemProps = {
  transaction: Transaction;
  key: number;
};

const TransactionItem = (props: TransactionItemProps) => {
  const palette = paletteProvider();
  const transactionDate = new Date(props.transaction.transactionDate);
  const formattedDate = transactionDate.toLocaleDateString("pl-PL", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
  });
  const formattedTime = transactionDate.toLocaleTimeString("pl-PL", {
    hour: "2-digit",
    minute: "2-digit",
  });

  const transactionColorHandler = (type: string) => {
    if (type === "DEPOSIT") {
      return palette.secondary.main;
    } else if (type === "PAYOUT") {
      return palette.primary.light;
    } else {
      return palette.error.main;
    }
  };

  const transactionTypeHeaderStyle = {
    color: palette.primary.main,
    fontWeight: "bold",
    fontSize: props.transaction.amount < 1000 ? "1.2rem" : "1.1rem",
  };

  const transactionAmountStyle = {
    backgroundColor: transactionColorHandler(props.transaction.transactionType),
    padding: "0.5rem 1rem",
    borderRadius: "2rem",
    fontSize: props.transaction.amount < 1000 ? "1.5rem" : "1rem",
    fontWeight: "bold",
  };

  const transactionDateStyle = {
    color: palette.text.primary,
    fontSize: "0.8rem",
  };

  const paperStyle = {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    margin: 1,
    padding: 1,
    borderRadius: "16px",
    backgroundColor: "#e0f2f1",
    width: "45%",
    height: "10vh",
  };

  return (
    <Paper elevation={2} sx={paperStyle}>
      <Stack direction="column" justifyContent="center" spacing={0.5}>
        <Typography sx={transactionTypeHeaderStyle}>
          {props.transaction.transactionType}
        </Typography>
        <Typography sx={transactionDateStyle}>{formattedDate}</Typography>
        <Typography sx={transactionDateStyle}>{formattedTime}</Typography>
      </Stack>
      <Typography color={palette.text.secondary} sx={transactionAmountStyle}>
        {`${props.transaction.amount.toFixed(2)} zł`}
      </Typography>
    </Paper>
  );
};

export default TransactionItem;
