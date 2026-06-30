import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function Home() {
    const { usuario, logout } = useAuth();
    const navigate = useNavigate();

    function handleLogout() {
        logout();
        navigate("/login");
    }

    // Os "cards" de navegacao. Por enquanto, sem destino real (vamos
    // ligar as telas depois). Cada um tera sua propria pagina.
    const menus = [
        { titulo: "Registro de Tag", descricao: "Leitura de ponto em tempo real", rota: "/registro-tag" },
        { titulo: "Funcionários", descricao: "Cadastro e gestão de colaboradores", rota: "/funcionarios" },
        { titulo: "Relatório Mensal", descricao: "Espelho de ponto e saldo de horas", rota: "/relatorio" },
        { titulo: "Faltosos do Dia", descricao: "Quem ainda não registrou ponto", rota: "/faltosos" },
    ];

    return (
        <div className="min-h-screen bg-neutral-100">
            {/* Cabecalho */}
            <header className="bg-neutral-900 text-white">
                <div className="max-w-6xl mx-auto px-6 py-4 flex items-center justify-between">
                    <h1 className="text-xl font-bold">
                        Kaema <span className="text-orange-500">Ponto</span>
                    </h1>
                    <div className="flex items-center gap-4">
            <span className="text-sm text-neutral-300">
              {usuario?.name}
            </span>
                        <button
                            onClick={handleLogout}
                            className="text-sm bg-neutral-700 hover:bg-neutral-600 px-4 py-2 rounded-lg transition"
                        >
                            Sair
                        </button>
                    </div>
                </div>
            </header>

            {/* Conteudo */}
            <main className="max-w-6xl mx-auto px-6 py-10">
                <h2 className="text-2xl font-bold text-neutral-900 mb-1">
                    Bem-vindo, {usuario?.name}!
                </h2>
                <p className="text-neutral-500 mb-8">
                    Selecione uma opção para começar.
                </p>

                {/* Grade de cards de navegacao */}
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
                    {menus.map((menu) => (
                        <button
                            key={menu.rota}
                            onClick={() => navigate(menu.rota)}
                            className="text-left bg-white rounded-xl shadow-sm hover:shadow-md border border-neutral-200 hover:border-orange-300 p-6 transition group"
                        >
                            <div className="w-10 h-10 rounded-lg bg-orange-100 group-hover:bg-orange-500 transition mb-4 flex items-center justify-center">
                <span className="text-orange-500 group-hover:text-white font-bold transition">
                  {menu.titulo.charAt(0)}
                </span>
                            </div>
                            <h3 className="font-semibold text-neutral-900 mb-1">
                                {menu.titulo}
                            </h3>
                            <p className="text-sm text-neutral-500">
                                {menu.descricao}
                            </p>
                        </button>
                    ))}
                </div>
            </main>
        </div>
    );
}

export default Home;