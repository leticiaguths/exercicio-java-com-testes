package org.example.repository;

import org.example.model.Produto;
import org.example.util.ConexaoBanco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepositoryImpl implements ProdutoRepository{

    @Override
    public Produto save(Produto produto) throws SQLException {

        String query = "INSERT INTO produto (nome, preco, quantidade, categoria) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setString(4, produto.getCategoria());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Falha ao cadastrar produto, nenhuma linha afetada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    produto.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Falha ao cadastrar produto, ID não obtido.");
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return produto;
    }

    @Override
    public List<Produto> findAll() throws SQLException {

        String query = "SELECT id, nome, preco, quantidade, categoria FROM produto";
        List<Produto> listaProdutos = new ArrayList<>();

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)){
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                listaProdutos.add(searchProducts(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return listaProdutos;
    }

    private Produto searchProducts(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.setId(rs.getInt("id"));
        produto.setNome(rs.getString("nome"));
        produto.setPreco(rs.getDouble("preco"));
        produto.setQuantidade(rs.getInt("quantidade"));
        produto.setCategoria(rs.getString("categoria"));
        return produto;
    }

    @Override
    public Produto findById(int id) throws SQLException {

        String query = "SELECT id, nome, preco, quantidade, categoria FROM produto WHERE id = ?";
        Produto produto = new Produto();

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                produto = searchProducts(rs);
            } else {
                return null;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return produto;
    }

    @Override
    public Produto update(Produto produto, int id) throws SQLException {

        String query = "UPDATE produto SET nome = ?, preco = ?, quantidade = ?, categoria = ? WHERE id = ?";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setString(4, produto.getCategoria());
            stmt.setInt(5, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Nenhum produto encontrado para o ID: " + produto.getId());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public boolean deleteById(int id) throws SQLException {

        String query = "DELETE FROM produto WHERE id = ?";
        int affectedRows;

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            affectedRows = stmt.executeUpdate();

        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return affectedRows > 0;
    }
}
