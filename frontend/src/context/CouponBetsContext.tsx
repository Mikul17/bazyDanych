import { Bet } from "@/constants/Types";
import {
  createContext,
  ReactNode,
  useContext,
  useEffect,
  useState,
} from "react";

interface BetsContextProps {
  bets: Bet[];
  addBet: (bet: Bet) => void;
  removeBet: (betId: number) => void;
  clearBets: () => void;
}

const BetsContext = createContext<BetsContextProps>({
  bets: [],
  addBet: () => {},
  removeBet: () => {},
  clearBets: () => {},
});

const compareBets = (bet1: Bet, bet2: Bet) => {
  const { betType } = bet1;
  const { betType: betType2 } = bet2;

  if (betType.betStat === "score" && betType2.betStat === "score") {
    return (
      betType.betStat === betType2.betStat &&
      betType.betTypeCode === betType2.betTypeCode
    );
  } else {
    return (
      betType.betStat === betType2.betStat &&
      betType.betTypeCode === betType2.betTypeCode &&
      betType.team === betType2.team
    );
  }
};

export const useBets = () => useContext(BetsContext);

export const BetsProvider = ({ children }: { children: ReactNode }) => {
  const [bets, setBets] = useState<Bet[]>([]);

  useEffect(() => {
    const storedBets = JSON.parse(localStorage.getItem("bets") || "[]");
    setBets(storedBets);
  }, []);
  const addBet = (newBet: Bet) => {
    const existingBetIndex = bets.findIndex((bet) => compareBets(bet, newBet));

    let updatedBets;
    if (existingBetIndex !== -1) {
        updatedBets = [...bets];
        updatedBets[existingBetIndex] = newBet;
    } else {
        updatedBets = [...bets, newBet];
    }

    setBets(updatedBets);
    localStorage.setItem('bets', JSON.stringify(updatedBets));
    console.log(updatedBets);
};

  const removeBet = (betId: number) => {
    const updatedBets = bets.filter((bet) => bet.id !== betId);
    setBets(updatedBets);
    localStorage.setItem("bets", JSON.stringify(updatedBets));
  };

  const clearBets = () => {
    setBets([]);
    localStorage.setItem("bets", JSON.stringify([]));
  };

  return (
    <BetsContext.Provider value={{ bets, addBet, removeBet, clearBets }}>
      {children}
    </BetsContext.Provider>
  );
};
