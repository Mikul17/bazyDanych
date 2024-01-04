"use client"
import paletteProvider from "@/constants/color-palette";
import {
    Alert,
  Box,
  Button,
  Container,
  Fade,
  Link,
  Modal,
  Paper,
  TextField,
  Typography,

} from "@mui/material";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/context/AuthContext";
import { buttonStyle, headerStyle, inputStyle } from "@/constants/Styles";
import { EmailOutlined, Login } from "@mui/icons-material";

interface alertState {
    message : string;
    type: "success"|"error"|''|"info";
}


const LoginForm = () => {
  const palette = paletteProvider();
  const [email, setEmail] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [alertState, setAlertState] = useState<alertState>({message:"", type:''});
  const [openModal, setOpenModal] = useState<boolean>(false);
  const [resetEmail, setResetEmail] = useState('');
  const router = useRouter();
  const { logIn } = useAuth();

  const handleOpenModal = ():void => setOpenModal(true);
  const handleCloseModal = ():void => {
    setOpenModal(false);
    setResetEmail('');
    setAlertState({message:"", type:''});
  };

  const handlePasswordResetClick = (event: React.MouseEvent<HTMLAnchorElement>) => {
    event.preventDefault();
    handleOpenModal();
  };
  const handleSubmit = async (event: {
    preventDefault: () => void;
    currentTarget: HTMLFormElement | undefined;
  }) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const username = data.get("username");
    const password = data.get("password");
    setEmail('');
    setPassword('');

    try{
        const response = await fetch('http://localhost:8080/api/auth/login',{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                email:username,
                password:password,
            }),
        });
        if (response.ok) {
            const jsonResponse = await response.json();
            // sessionStorage.setItem('token', jsonResponse.token);
            logIn(jsonResponse.token);
            router.push("/");
          } else {
            setAlertState({message:"Login failed. Email or password incorrect.", type:"error"});
          }
        } catch (error) {
          setAlertState({message:"Error while connecting to server", type:"error"});
        }
    };

    const handlePasswordReset = async (event: React.MouseEvent<HTMLButtonElement>) => {
      event.preventDefault();

      const encodedEmail = encodeURIComponent(resetEmail);
      const url = `http://localhost:8080/api/user/resetRequest?email=${encodedEmail}`;
      try{
        const response = await fetch(url,{
            method: 'PUT',
            
        });
        if (response.ok) {
            setResetEmail('');
            setAlertState({message:"A password reset link will be sent to your email.", type:"info"});
          } else {
            const message = await response.text();
            setAlertState({message:"Password reset failed: "+message, type:"error"});
          }
        } catch (errorResponse) {
          if (errorResponse instanceof Response) {
            const errorMessage = await errorResponse.text();
            setAlertState({message:"Error while connecting to server: "+errorMessage, type:"error"});
        }
            else{
              setAlertState({message:"Error while sending request", type:"error"});
            }
        }
    };


  const paperStyle = {
    marginTop: "10rem",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    padding: "2rem",
    paddingTop:"0.5rem",
    backgroundColor:palette.primary.light,
    borderRadius:"1.25rem"
  };


  const modalBoxStyle = {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    padding: "0 1rem 1rem 1rem",
    backgroundColor:palette.primary.main,
    borderRadius:"1.25rem",
    width:"25%",
    margin:"auto",
    marginTop:"10rem",
  };

  return (
    <Container component="main" maxWidth="xs">
      <Paper elevation={3} sx={paperStyle}>
        <Box width={"100%"} sx={headerStyle("center")}>
        <Typography component="h1" variant="h5" fontWeight={"bold"} color={palette.primary.main}>
          Login
        </Typography>
        </Box>
        <form onSubmit={handleSubmit} style={{ width: "100%", marginTop: 1 }}>
          <TextField
            variant="outlined"
            margin="normal"
            fullWidth
            id="username"
            label="Username"
            name="username"
            autoComplete="username"
            autoFocus
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            InputLabelProps={{sx:{"&.Mui-focused": {
              color: palette.text.secondary,
            },}}}
            sx={inputStyle}
          />
          <TextField
            variant="outlined"
            margin="normal"
            fullWidth
            name="password"
            label="Password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            autoComplete="current-password"
            InputLabelProps={{sx:{"&.Mui-focused": {
              color: palette.text.secondary,
            },}}}
            sx={inputStyle}
          />
          <Button
            type="submit"
            fullWidth
            variant="contained"
            color="primary"
            style={buttonStyle(palette.secondary.main)}
            endIcon={<Login/>}
          >
            Login
            {/* <Login sx={{marginLeft:"0.25rem"}}/> */}
          </Button>
          <Box
            display={"flex"}
            justifyContent={"space-around"}
            alignContent={"center"}
          >
            <Link href="#" onClick={handlePasswordResetClick} variant="body2" sx={{color:"white", textDecoration:"none"}}>
              Forgot password?
            </Link>
            <Link href="/register" variant="body2" sx={{color:"white", display:"flex", textDecoration:"none"}}>
              <Typography variant="body2" sx={{color: palette.primary.dark}}>Sign up!</Typography>
            </Link>
          </Box>
        </form>
      </Paper>
      {alertState.message && 
      <Alert variant="outlined" severity="error" onClose={() => setAlertState({ message: '', type: '' })} sx={{marginTop:"2rem"}}>{alertState.message}</Alert>
      }
      <Modal
      open={openModal}
      onClose={handleCloseModal}
      closeAfterTransition
    >
      <Fade in={openModal}>
        <Box sx={modalBoxStyle}>
          <Box width="100%" sx={headerStyle("center")}>
          <Typography variant="h6" fontWeight={"bold"}>Reset Password</Typography>
          </Box>
          <TextField
            autoFocus
            margin="dense"
            id="resetEmail"
            label="Email Address"
            type="email"
            fullWidth
            variant="outlined"
            sx={inputStyle}
            value={resetEmail}
            InputLabelProps={{sx:{"&.Mui-focused": {
              color: palette.text.secondary,
            },}}}
            onChange={(e) => setResetEmail(e.target.value)}
          />
          <Button sx={buttonStyle(palette.secondary.main)} onClick={handlePasswordReset} endIcon={<EmailOutlined/>}>Send Reset Link</Button>
          {alertState.message && 
          <Alert variant="filled" severity={alertState.type==="info"?"info":"error"} sx={{marginTop:"1rem"}}>{alertState.message}</Alert>}
          
        </Box>
      </Fade>
    </Modal>
      
    </Container>
    
  );
};

export default LoginForm;
