package br.com.agibank.teste.pedidos.controller;

import br.com.agibank.teste.pedidos.dto.ProdutoRequestDTO;
import br.com.agibank.teste.pedidos.dto.ProdutoResponseDTO;
import br.com.agibank.teste.pedidos.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> salvarProduto(@RequestBody @Valid ProdutoRequestDTO produtoRequestDTO){
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.salvar(produtoRequestDTO));
    }

    @GetMapping("/{idProduto}")
    public ResponseEntity<ProdutoResponseDTO> findById(@PathVariable Long idProduto){
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.findById(idProduto));
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoResponseDTO>> findAll(@PageableDefault(size = 10) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.findAll(pageable));
    }

    @DeleteMapping("/{idProduto}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long idProduto){
        produtoService.deletar(idProduto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/buscar/nome")
    public ResponseEntity<Page<ProdutoResponseDTO>> findByNome(@RequestParam String nome, @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(produtoService.findByNome(nome, pageable));
    }

    @PutMapping
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(@RequestBody @Valid ProdutoRequestDTO produtoRequestDTO){
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.atualizar(produtoRequestDTO));
    }
}
