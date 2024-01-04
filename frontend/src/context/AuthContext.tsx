import React, { createContext, useContext, useState, ReactNode, FC, useEffect } from 'react';

interface AuthContextType {
  isLogged: boolean;
  logIn: (token: string) => void;
  logOut: () => void;
}

const AuthContext = createContext<AuthContextType>({} as AuthContextType);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: FC<AuthProviderProps> = ({ children }) => {
  const [isLogged, setIsLogged] = useState<boolean>(false);

  useEffect(() => {
    
    setIsLogged(sessionStorage.getItem("token") !== null);
  }, []);

  const logIn = (token: string) => {
    if (typeof window !== 'undefined') {
      sessionStorage.setItem("token", token);
      setIsLogged(true);
    }
  };

  const logOut = () => {
    if (typeof window !== 'undefined') {
      sessionStorage.removeItem("token");
      setIsLogged(false);
    }
  };

  return (
    <AuthContext.Provider value={{ isLogged, logIn, logOut }}>
      {children}
    </AuthContext.Provider>
  );
};
