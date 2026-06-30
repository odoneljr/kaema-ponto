import { useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import api from "../services/api";

function FuncionarioForm() {
    const navigate = useNavigate();

    const [searchParams] = useSearchParams();
    const tagInicial = searchParams.get("tag") || "";
    // Um unico objeto com todos os campos do formulario.
    const [form, setForm] = useState({
        name: "",
        email: "",
        password: "",
        confirmarSenha: "",
        isAdmin: false,
        cpf: "",
        rg: "",
        ctps: "",
        pispasep: "",
        cargo: "",
        gestor: "",
        centrocusto: "",
        startDate: "",
        tagRfid: tagInicial,
    });

    const [erro, setErro] = useState("");
    const [salvando, setSalvando] = useState(false);

    // Atualiza um campo do form. Funciona para texto e checkbox.
    function handleChange(e) {
        const { name, value, type, checked } = e.target;
        setForm((anterior) => ({
            ...anterior,
            [name]: type === "checkbox" ? checked : value,
        }));
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setErro("");

        // Validacao simples: senhas batem?
        if (form.password !== form.confirmarSenha) {
            setErro("As senhas não coincidem.");
            return;
        }

        setSalvando(true);
        try {
            // Monta o objeto que o backend espera (sem confirmarSenha).
            const payload = {
                name: form.name,
                email: form.email,
                password: form.password,
                startDate: form.startDate,
                cpf: form.cpf,
                rg: form.rg,
                ctps: form.ctps,
                pispasep: form.pispasep,
                cargo: form.cargo,
                gestor: form.gestor,
                centrocusto: form.centrocusto,
                tagRfid: form.tagRfid || null, // vazio vira null
                isAdmin: form.isAdmin,
            };

            await api.post("/funcionarios", payload);
            // Sucesso: volta para a lista.
            navigate("/funcionarios");
        } catch (err) {
            // Erros comuns: email/cpf/rg/tag duplicados.
            setErro(
                "Não foi possível cadastrar. Verifique se email, CPF, RG ou tag já existem."
            );
        } finally {
            setSalvando(false);
        }
    }

    // Classe reutilizavel para os inputs.
    const inputClass =
        "w-full px-4 py-2.5 rounded-lg border border-neutral-300 focus:border-orange-500 focus:ring-2 focus:ring-orange-200 outline-none transition";
    const labelClass = "block text-sm font-medium text-neutral-700 mb-1";

    return (
        <div className="min-h-screen bg-neutral-100">
            {/* Cabecalho */}
            <header className="bg-neutral-900 text-white">
                <div className="max-w-4xl mx-auto px-6 py-4 flex items-center justify-between">
                    <h1 className="text-xl font-bold">
                        Kaema <span className="text-orange-500">Ponto</span>
                    </h1>
                    <button
                        onClick={() => navigate("/funcionarios")}
                        className="text-sm bg-neutral-700 hover:bg-neutral-600 px-4 py-2 rounded-lg transition"
                    >
                        Voltar
                    </button>
                </div>
            </header>

            {/* Formulario */}
            <main className="max-w-4xl mx-auto px-6 py-10">
                <h2 className="text-2xl font-bold text-neutral-900 mb-1">
                    Novo Funcionário
                </h2>
                <p className="text-neutral-500 mb-8">
                    Preencha os dados do colaborador.
                </p>

                <form onSubmit={handleSubmit} className="space-y-8">
                    {/* Secao: Dados de acesso */}
                    <section className="bg-white rounded-xl shadow-sm border border-neutral-200 p-6">
                        <h3 className="font-semibold text-neutral-900 mb-4">
                            Dados de acesso
                        </h3>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div className="md:col-span-2">
                                <label className={labelClass}>Nome completo</label>
                                <input name="name" value={form.name} onChange={handleChange} required className={inputClass} />
                            </div>
                            <div className="md:col-span-2">
                                <label className={labelClass}>Email</label>
                                <input type="email" name="email" value={form.email} onChange={handleChange} required className={inputClass} />
                            </div>
                            <div>
                                <label className={labelClass}>Senha</label>
                                <input type="password" name="password" value={form.password} onChange={handleChange} required className={inputClass} />
                            </div>
                            <div>
                                <label className={labelClass}>Confirmar senha</label>
                                <input type="password" name="confirmarSenha" value={form.confirmarSenha} onChange={handleChange} required className={inputClass} />
                            </div>
                            <div className="md:col-span-2 flex items-center gap-2 mt-2">
                                <input type="checkbox" id="isAdmin" name="isAdmin" checked={form.isAdmin} onChange={handleChange} className="w-4 h-4 accent-orange-500" />
                                <label htmlFor="isAdmin" className="text-sm text-neutral-700">
                                    Administrador (acesso total ao sistema)
                                </label>
                            </div>
                        </div>
                    </section>

                    {/* Secao: Documentos */}
                    <section className="bg-white rounded-xl shadow-sm border border-neutral-200 p-6">
                        <h3 className="font-semibold text-neutral-900 mb-4">Documentos</h3>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <label className={labelClass}>CPF</label>
                                <input name="cpf" value={form.cpf} onChange={handleChange} required className={inputClass} />
                            </div>
                            <div>
                                <label className={labelClass}>RG</label>
                                <input name="rg" value={form.rg} onChange={handleChange} required className={inputClass} />
                            </div>
                            <div>
                                <label className={labelClass}>CTPS</label>
                                <input name="ctps" value={form.ctps} onChange={handleChange} required className={inputClass} />
                            </div>
                            <div>
                                <label className={labelClass}>PIS/PASEP</label>
                                <input name="pispasep" value={form.pispasep} onChange={handleChange} required className={inputClass} />
                            </div>
                        </div>
                    </section>

                    {/* Secao: Dados profissionais */}
                    <section className="bg-white rounded-xl shadow-sm border border-neutral-200 p-6">
                        <h3 className="font-semibold text-neutral-900 mb-4">
                            Dados profissionais
                        </h3>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <label className={labelClass}>Cargo</label>
                                <input name="cargo" value={form.cargo} onChange={handleChange} required className={inputClass} />
                            </div>
                            <div>
                                <label className={labelClass}>Gestor</label>
                                <input name="gestor" value={form.gestor} onChange={handleChange} required className={inputClass} />
                            </div>
                            <div>
                                <label className={labelClass}>Centro de custo</label>
                                <input name="centrocusto" value={form.centrocusto} onChange={handleChange} required className={inputClass} />
                            </div>
                            <div>
                                <label className={labelClass}>Data de admissão</label>
                                <input type="date" name="startDate" value={form.startDate} onChange={handleChange} required className={inputClass} />
                            </div>
                            <div className="md:col-span-2">
                                <label className={labelClass}>Tag RFID (opcional)</label>
                                <input name="tagRfid" value={form.tagRfid} onChange={handleChange} className={inputClass} placeholder="Deixe vazio para vincular depois" />
                            </div>
                        </div>
                    </section>

                    {/* Erro */}
                    {erro && (
                        <p className="text-sm text-red-600 bg-red-50 rounded-lg px-4 py-3">
                            {erro}
                        </p>
                    )}

                    {/* Botoes */}
                    <div className="flex gap-3">
                        <button
                            type="button"
                            onClick={() => navigate("/funcionarios")}
                            className="px-6 py-2.5 rounded-lg bg-neutral-200 hover:bg-neutral-300 text-neutral-800 font-semibold transition"
                        >
                            Cancelar
                        </button>
                        <button
                            type="submit"
                            disabled={salvando}
                            className="px-6 py-2.5 rounded-lg bg-orange-500 hover:bg-orange-600 text-white font-semibold transition disabled:opacity-60"
                        >
                            {salvando ? "Salvando..." : "Salvar funcionário"}
                        </button>
                    </div>
                </form>
            </main>
        </div>
    );
}

export default FuncionarioForm;