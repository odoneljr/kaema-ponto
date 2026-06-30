import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function RegistroTag() {
    const navigate = useNavigate();

    // Guarda o evento de leitura mais recente (para exibir o pop-up).
    // null = nenhum pop-up aberto.
    const [evento, setEvento] = useState(null);

    // useEffect com [] roda UMA vez, quando a tela abre.
    // E aqui que abrimos a conexao SSE com o backend.
    useEffect(() => {
        const API_URL = import.meta.env.VITE_API_URL;

        // EventSource e a API nativa do navegador para SSE.
        const eventSource = new EventSource(`${API_URL}/eventos/leituras`);

        // Escuta eventos com nome "leitura" (o nome que demos no backend).
        eventSource.addEventListener("leitura", (e) => {
            const dados = JSON.parse(e.data); // o LeituraEvento do backend
            setEvento(dados); // abre o pop-up com esses dados
        });

        // Limpeza: ao sair da tela, fecha a conexao.
        return () => {
            eventSource.close();
        };
    }, []);

    // Fecha o pop-up.
    function fecharPopup() {
        setEvento(null);
    }

    return (
        <div className="min-h-screen bg-neutral-100">
            {/* Cabecalho */}
            <header className="bg-neutral-900 text-white">
                <div className="max-w-6xl mx-auto px-6 py-4 flex items-center justify-between">
                    <h1 className="text-xl font-bold">
                        Kaema <span className="text-orange-500">Ponto</span>
                    </h1>
                    <button
                        onClick={() => navigate("/home")}
                        className="text-sm bg-neutral-700 hover:bg-neutral-600 px-4 py-2 rounded-lg transition"
                    >
                        Voltar
                    </button>
                </div>
            </header>

            {/* Area de leitura */}
            <main className="max-w-3xl mx-auto px-6 py-16 text-center">
                <div className="bg-white rounded-2xl shadow-sm border border-neutral-200 p-16">
                    <div className="inline-flex items-center gap-2 text-sm text-green-600 bg-green-50 px-4 py-2 rounded-full mb-8">
                        <span className="w-2 h-2 bg-green-500 rounded-full animate-pulse"></span>
                        Aguardando leitura de tag...
                    </div>

                    <h2 className="text-2xl font-bold text-neutral-900 mb-2">
                        Registro de Ponto
                    </h2>
                    <p className="text-neutral-500">
                        Aproxime a tag RFID do leitor para registrar o ponto.
                    </p>
                </div>
            </main>

            {/* POP-UP: so aparece quando ha um evento */}
            {evento && (
                <div className="fixed inset-0 bg-black/50 flex items-center justify-center px-4 z-50">
                    <div className="bg-white rounded-2xl shadow-2xl max-w-md w-full p-8 text-center">
                        {evento.tipo === "VINCULADA" ? (
                            // --- Pop-up: tag vinculada ---
                            <>
                                <div className="w-16 h-16 rounded-full bg-green-100 flex items-center justify-center mx-auto mb-4">
                                    <span className="text-green-600 text-3xl">✓</span>
                                </div>
                                <h3 className="text-2xl font-bold text-neutral-900 mb-1">
                                    {evento.nomeFuncionario}
                                </h3>
                                <p className="text-neutral-500 mb-1">Ponto registrado com sucesso</p>
                                <p className="text-4xl font-bold text-orange-500 my-4">
                                    {evento.horario}
                                </p>
                                <button
                                    onClick={fecharPopup}
                                    className="w-full bg-neutral-900 hover:bg-neutral-800 text-white font-semibold py-2.5 rounded-lg transition"
                                >
                                    Fechar
                                </button>
                            </>
                        ) : (
                            // --- Pop-up: tag NAO vinculada ---
                            <>
                                <div className="w-16 h-16 rounded-full bg-orange-100 flex items-center justify-center mx-auto mb-4">
                                    <span className="text-orange-500 text-3xl">!</span>
                                </div>
                                <h3 className="text-xl font-bold text-neutral-900 mb-1">
                                    Tag não vinculada
                                </h3>
                                <p className="text-neutral-500 mb-1">
                                    A tag <span className="font-mono font-semibold">{evento.codigoTag}</span> não
                                    está associada a nenhum funcionário.
                                </p>
                                <div className="flex gap-3 mt-6">
                                    <button
                                        onClick={fecharPopup}
                                        className="flex-1 bg-neutral-200 hover:bg-neutral-300 text-neutral-800 font-semibold py-2.5 rounded-lg transition"
                                    >
                                        Fechar
                                    </button>
                                    <button
                                        onClick={() => {
                                            // TODO: navegar para cadastro com a tag pre-preenchida
                                            fecharPopup();
                                        }}
                                        className="flex-1 bg-orange-500 hover:bg-orange-600 text-white font-semibold py-2.5 rounded-lg transition"
                                    >
                                        Cadastrar
                                    </button>
                                </div>
                            </>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
}

export default RegistroTag;