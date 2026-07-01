import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";

function RelatorioMensal() {
    const navigate = useNavigate();

    // Lista de funcionarios (para o seletor).
    const [funcionarios, setFuncionarios] = useState([]);
    // Selecoes atuais.
    const [userId, setUserId] = useState("");
    const [ano, setAno] = useState(new Date().getFullYear());
    const [mes, setMes] = useState(new Date().getMonth() + 1); // 1-12
    // Dados do relatorio.
    const [dias, setDias] = useState([]);
    const [carregando, setCarregando] = useState(false);
    const [erro, setErro] = useState("");

    // Ao abrir, carrega a lista de funcionarios para o dropdown.
    useEffect(() => {
        async function carregarFuncionarios() {
            try {
                const resp = await api.get("/funcionarios");
                setFuncionarios(resp.data);
            } catch {
                setErro("Não foi possível carregar os funcionários.");
            }
        }
        carregarFuncionarios();
    }, []);

    // Busca o relatorio quando o usuario clica em "Buscar".
    async function buscarRelatorio() {
        if (!userId) {
            setErro("Selecione um funcionário.");
            return;
        }
        setErro("");
        setCarregando(true);
        try {
            const resp = await api.get("/relatorio", {
                params: { userId, ano, mes },
            });
            setDias(resp.data);
        } catch {
            setErro("Não foi possível carregar o relatório.");
        } finally {
            setCarregando(false);
        }
    }

    // Formata "2026-06-01" -> "seg, 01/06"
    function formatarDia(dataStr) {
        const data = new Date(dataStr + "T00:00:00");
        const diaSemana = data.toLocaleDateString("pt-BR", { weekday: "short" });
        const diaMes = data.toLocaleDateString("pt-BR", {
            day: "2-digit",
            month: "2-digit",
        });
        return { diaSemana, diaMes, ehFimDeSemana: [0, 6].includes(data.getDay()) };
    }

    // Formata "08:00:00" -> "08:00" (ou "--" se nulo)
    function hora(valor) {
        return valor ? valor.slice(0, 5) : "--";
    }

    const meses = [
        "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
        "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro",
    ];

    const selectClass =
        "px-4 py-2.5 rounded-lg border border-neutral-300 focus:border-orange-500 focus:ring-2 focus:ring-orange-200 outline-none transition bg-white";

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

            {/* Conteudo */}
            <main className="max-w-6xl mx-auto px-6 py-10">
                <h2 className="text-2xl font-bold text-neutral-900 mb-1">
                    Relatório Mensal
                </h2>
                <p className="text-neutral-500 mb-6">
                    Espelho de ponto do funcionário no mês.
                </p>

                {/* Filtros */}
                <div className="bg-white rounded-xl shadow-sm border border-neutral-200 p-5 mb-6 flex flex-wrap items-end gap-4">
                    <div>
                        <label className="block text-sm font-medium text-neutral-700 mb-1">
                            Funcionário
                        </label>
                        <select
                            value={userId}
                            onChange={(e) => setUserId(e.target.value)}
                            className={selectClass}
                        >
                            <option value="">Selecione...</option>
                            {funcionarios.map((f) => (
                                <option key={f.id} value={f.id}>
                                    {f.name}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-neutral-700 mb-1">
                            Mês
                        </label>
                        <select
                            value={mes}
                            onChange={(e) => setMes(Number(e.target.value))}
                            className={selectClass}
                        >
                            {meses.map((nome, i) => (
                                <option key={i} value={i + 1}>
                                    {nome}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-neutral-700 mb-1">
                            Ano
                        </label>
                        <input
                            type="number"
                            value={ano}
                            onChange={(e) => setAno(Number(e.target.value))}
                            className={selectClass + " w-28"}
                        />
                    </div>

                    <button
                        onClick={buscarRelatorio}
                        className="bg-orange-500 hover:bg-orange-600 text-white font-semibold px-6 py-2.5 rounded-lg transition"
                    >
                        Buscar
                    </button>
                </div>

                {/* Erro */}
                {erro && (
                    <p className="text-red-600 bg-red-50 rounded-lg px-4 py-3 mb-6">{erro}</p>
                )}

                {/* Tabela */}
                {carregando ? (
                    <p className="text-neutral-500">Carregando...</p>
                ) : dias.length > 0 ? (
                    <div className="bg-white rounded-xl shadow-sm border border-neutral-200 overflow-x-auto">
                        <table className="w-full text-left text-sm">
                            <thead className="bg-neutral-50 text-neutral-600">
                            <tr>
                                <th className="px-4 py-3 font-medium">Dia</th>
                                <th className="px-4 py-3 font-medium">Ent 1</th>
                                <th className="px-4 py-3 font-medium">Saí 1</th>
                                <th className="px-4 py-3 font-medium">Ent 2</th>
                                <th className="px-4 py-3 font-medium">Saí 2</th>
                                <th className="px-4 py-3 font-medium">Ent 3</th>
                                <th className="px-4 py-3 font-medium">Saí 3</th>
                                <th className="px-4 py-3 font-medium">Observação</th>
                            </tr>
                            </thead>
                            <tbody className="divide-y divide-neutral-100">
                            {dias.map((d) => {
                                const { diaSemana, diaMes, ehFimDeSemana } = formatarDia(d.data);
                                return (
                                    <tr
                                        key={d.data}
                                        className={ehFimDeSemana ? "bg-neutral-50/60" : ""}
                                    >
                                        <td className="px-4 py-3 whitespace-nowrap">
                                            <span className="font-medium text-neutral-900">{diaMes}</span>
                                            <span className="text-neutral-400 ml-2 capitalize">
                          {diaSemana}
                        </span>
                                        </td>
                                        <td className="px-4 py-3 font-mono">{hora(d.time1)}</td>
                                        <td className="px-4 py-3 font-mono">{hora(d.time2)}</td>
                                        <td className="px-4 py-3 font-mono">{hora(d.time3)}</td>
                                        <td className="px-4 py-3 font-mono">{hora(d.time4)}</td>
                                        <td className="px-4 py-3 font-mono">{hora(d.time5)}</td>
                                        <td className="px-4 py-3 font-mono">{hora(d.time6)}</td>
                                        <td className="px-4 py-3 text-neutral-500">
                                            {d.observacao || ""}
                                        </td>
                                    </tr>
                                );
                            })}
                            </tbody>
                        </table>
                    </div>
                ) : (
                    <p className="text-neutral-500">
                        Selecione um funcionário e o mês, depois clique em Buscar.
                    </p>
                )}
            </main>
        </div>
    );
}

export default RelatorioMensal;