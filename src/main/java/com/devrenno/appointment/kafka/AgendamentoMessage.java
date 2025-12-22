package com.devrenno.appointment.kafka;

public class AgendamentoMessage {
    private String emailPaciente;
    private String nomePaciente;
    private String dataAtendimento; // Formato ddmmaaaa
    private String horaAtendimento; // Formato hh:mm
    private String nomeResponsavel;

    public AgendamentoMessage() {
    }

    public AgendamentoMessage(String emailPaciente, String nomePaciente, String dataAtendimento, String horaAtendimento, String nomeResponsavel) {
        this.emailPaciente = emailPaciente;
        this.nomePaciente = nomePaciente;
        this.dataAtendimento = dataAtendimento;
        this.horaAtendimento = horaAtendimento;
        this.nomeResponsavel = nomeResponsavel;
    }

    public String getEmailPaciente() {
        return emailPaciente;
    }

    public void setEmailPaciente(String emailPaciente) {
        this.emailPaciente = emailPaciente;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public String getDataAtendimento() {
        return dataAtendimento;
    }

    public void setDataAtendimento(String dataAtendimento) {
        this.dataAtendimento = dataAtendimento;
    }

    public String getHoraAtendimento() {
        return horaAtendimento;
    }

    public void setHoraAtendimento(String horaAtendimento) {
        this.horaAtendimento = horaAtendimento;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }
}

