import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import RotaProtegida from "./routes/RotaProtegida";
import Login from "./pages/Login";
import Home from "./pages/Home";
import RegistroTag from "./pages/RegistroTag";
import Funcionarios from "./pages/Funcionarios";
import FuncionarioForm from "./pages/FuncionarioForm";
import Faltosos from "./pages/Faltosos";
import RelatorioMensal from "./pages/RelatorioMensal";

function App() {
    return (
        <AuthProvider>
            <BrowserRouter>
                <Routes>
                    {/* Rota raiz redireciona para o login */}
                    <Route path="/" element={<Navigate to="/login" />} />

                    {/* Rota publica */}
                    <Route path="/login" element={<Login />} />

                    {/* Rota protegida: so abre se autenticado */}
                    <Route
                        path="/home"
                        element={
                            <RotaProtegida>
                                <Home />
                            </RotaProtegida>
                        }
                    />

                    <Route
                        path="/registro-tag"
                        element={
                            <RotaProtegida>
                                <RegistroTag />
                            </RotaProtegida>
                        }
                    />

                    <Route
                        path="/funcionarios"
                        element={
                            <RotaProtegida>
                                <Funcionarios />
                            </RotaProtegida>
                        }
                    />

                    <Route
                        path="/funcionarios/novo"
                        element={
                            <RotaProtegida>
                                <FuncionarioForm />
                            </RotaProtegida>
                        }
                    />

                    <Route
                        path="/faltosos"
                        element={
                            <RotaProtegida>
                                <Faltosos />
                            </RotaProtegida>
                        }
                    />

                    <Route
                        path="/relatorio"
                        element={
                            <RotaProtegida>
                                <RelatorioMensal />
                            </RotaProtegida>
                        }
                    />
                </Routes>
            </BrowserRouter>
        </AuthProvider>
    );
}

export default App;