package rewatch.rewatch.journal;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnalyzeController {

    private final PatternFinder patternFinder;

    public AnalyzeController(PatternFinder patternFinder) {
        this.patternFinder = patternFinder;
    }

    /** Goals + journal entries in, the checked pattern report out. */
    @PostMapping("/api/analyze")
    public AnalyzeResponse analyze(@RequestBody AnalyzeRequest request) {
        return patternFinder.analyze(request);
    }
}
