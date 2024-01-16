import { jwtDecode } from 'jwt-decode';
import React, { createContext, useContext, useState, ReactNode } from 'react';

interface BalanceContextType {
  balance: number;
  fetchBalance: () => void;
}

const BalanceContext = createContext<BalanceContextType>({
  balance: 0,
  fetchBalance: () => {},
});


export const useBalance = () => useContext(BalanceContext);

interface BalanceProviderProps {
  children: ReactNode;
}

export const BalanceProvider: React.FC<BalanceProviderProps> = ({ children }) => {
  const [balance, setBalance] = useState<number>(0.00);

 const fetchBalance = () => {
    const token = sessionStorage.getItem("token");
    if(!token) {throw new Error("No token found")}

    const decoded = jwtDecode<{ userId: number }>(token!);
    const userId = decoded.userId;
    const url = "http://localhost:8080/api/user/balance/" + userId;
    const options = {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token,
        },
        };

    try{
        fetch(url, options)
        .then((response) => response.json())
        .then((data) => {
            setBalance(data);
        });
    }catch(error){
        console.error(error);
    }
  };

  return (
    <BalanceContext.Provider value={{ balance, fetchBalance }}>
      {children}
    </BalanceContext.Provider>
  );
};
